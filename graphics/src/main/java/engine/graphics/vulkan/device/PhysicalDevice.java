package engine.graphics.vulkan.device;

import engine.graphics.vulkan.util.VulkanUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceMemoryProperties;

public class PhysicalDevice {

    private VkPhysicalDevice vk;

    private int apiVersion;
    private int driverVersion;
    private int venderId;
    private int deviceId;
    private String deviceName;

    public PhysicalDevice(VkPhysicalDevice vkPhysicalDevice){
        this.vk = vkPhysicalDevice;
        var prop = VkPhysicalDeviceProperties.calloc();
        VK10.vkGetPhysicalDeviceProperties(vk, prop);
        apiVersion = prop.apiVersion();
        driverVersion = prop.driverVersion();
        venderId = prop.vendorID();
        deviceId = prop.deviceID();
        deviceName = prop.deviceNameString();
        prop.free();
    }

    public VkPhysicalDevice getNativePhysicalDevice() {
        return vk;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public int getDriverVersion() {
        return driverVersion;
    }

    public int getVenderId() {
        return venderId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    private int queueFamilyCount = -1;

    private List<VkQueueFamilyProperties> queueFamilyProperties;

    public int getQueueFamilyCount(){
        if(queueFamilyCount == -1) {
            try(var stack = MemoryStack.stackPush()){
                var buf = stack.mallocInt(1);
                VK10.vkGetPhysicalDeviceQueueFamilyProperties(vk, buf, null);
                queueFamilyCount = buf.get(0);
            }
        }
        return queueFamilyCount;
    }

    public List<VkQueueFamilyProperties> getQueueFamilyPropertiesList() {
        if(queueFamilyProperties == null) {
            try(var stack = MemoryStack.stackPush()){
                var size = stack.mallocInt(1).put(getQueueFamilyCount()).flip();
                var buf = VkQueueFamilyProperties.callocStack(getQueueFamilyCount(), stack);
                VK10.vkGetPhysicalDeviceQueueFamilyProperties(vk,size,buf);
                queueFamilyProperties = buf.stream().collect(Collectors.toList());
            }
        }
        return queueFamilyProperties;
    }

    public boolean doQueueFamilySupportGraphics(int index){
        if(index + 1 > getQueueFamilyCount()) return false;
        return (getQueueFamilyPropertiesList().get(index).queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0;
    }

    public boolean doQueueFamilySupportComputing(int index){
        if(index + 1 > getQueueFamilyCount()) return false;
        return (getQueueFamilyPropertiesList().get(index).queueFlags() & VK10.VK_QUEUE_COMPUTE_BIT) != 0;
    }

    public boolean doQueueFamilySupportTransferring(int index){
        if(index + 1 > getQueueFamilyCount()) return false;
        return (getQueueFamilyPropertiesList().get(index).queueFlags() & VK10.VK_QUEUE_TRANSFER_BIT) != 0;
    }

    private Map<String, String> supportedLayers = null;

    private Map<String, List<String>> supportedExtensions = new HashMap<>();

    public Map<String, String> getSupportedLayerProperties(){
        if(supportedLayers == null) {
            try(var stack = MemoryStack.stackPush()){
                var size = stack.mallocInt(1);
                var err = VK10.vkEnumerateDeviceLayerProperties(vk, size, null);
                if(err != VK_SUCCESS){
                    supportedLayers = new HashMap<>();
                }
                var ptrs = VkLayerProperties.callocStack(size.get(0), stack);
                err = VK10.vkEnumerateDeviceLayerProperties(vk, size, ptrs);
                if(err != VK_SUCCESS) {
                    supportedLayers = new HashMap<>();
                }
                supportedLayers = ptrs.stream().collect(Collectors.toMap(VkLayerProperties::layerNameString, VkLayerProperties::descriptionString));
            }
        }
        return supportedLayers;
    }

    public List<String> getSupportedExtensionProperties(@Nullable String layerName){
        if(layerName != null && !isLayerSupported(layerName)) return new ArrayList<>();
        return supportedExtensions.computeIfAbsent(layerName != null ? layerName : "", key -> {
            try (var stack = MemoryStack.stackPush()) {
                var size = stack.mallocInt(1);
                var err = VK10.vkEnumerateDeviceExtensionProperties(vk, layerName, size, null);
                if (err != VK_SUCCESS) {
                    return new ArrayList<>();
                }
                var ptrs = VkExtensionProperties.callocStack(size.get(0), stack);
                err = VK10.vkEnumerateDeviceExtensionProperties(vk, layerName, size, ptrs);
                if (err != VK_SUCCESS) {
                    return new ArrayList<>();
                }
                return ptrs.stream().map(VkExtensionProperties::extensionNameString).collect(Collectors.toList());
            }
        });
    }

    public boolean isLayerSupported(String layer){
        return getSupportedLayerProperties().containsKey(layer);
    }

    public boolean isExtensionSupported(String extension, @Nullable String layerName) {
        return getSupportedExtensionProperties(layerName).contains(extension);
    }

    public LogicalDevice createLogicalDevice(int[] queueFamilyIndices, int[] queueCount, @Nullable List<String> enabledExtensions, @Nullable List<String> layers){
        if (queueFamilyIndices.length != queueCount.length) throw new IllegalArgumentException("");
        if (queueFamilyIndices.length > getQueueFamilyCount()) throw new IllegalArgumentException("QueueFamily required exceed the number of it had!");
        if (queueFamilyIndices.length <= 0) throw new IllegalArgumentException("Assigning no QueueFamily is not allowed!");
        try(var stack = MemoryStack.stackPush()){
            var queueCreateInfos = VkDeviceQueueCreateInfo.callocStack(queueFamilyIndices.length, stack);
            for (int i = 0; i < queueFamilyIndices.length; i++) {
                var info = queueCreateInfos.get(i);
                var prior = stack.floats(0);
                info.sType(VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
                        .queueFamilyIndex(queueFamilyIndices[i]).pQueuePriorities(prior);
//                var count = Math.max(1,Math.min(queueCount[i], getQueueFamilyPropertiesList().get(queueFamilyIndices[i]).queueCount()));
//                VkDeviceQueueCreateInfo.nqueueCount(info.address(), count);
            }
            var deviceCreationInfo = VkDeviceCreateInfo.callocStack(stack);
            deviceCreationInfo.sType(VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO).flags(0).pQueueCreateInfos(queueCreateInfos);
            if(layers == null) {
                deviceCreationInfo.ppEnabledLayerNames(null);
            }
            else{
                var layersptr = stack.mallocPointer(layers.size());

                var iterator = layers.iterator();
                while (iterator.hasNext()) {
                    String enabledLayer = iterator.next();
                    if (!isLayerSupported(enabledLayer)) {
                        iterator.remove();
                    } else {
                        layersptr.put(MemoryStack.stackUTF8(enabledLayer));
                    }
                }
                layersptr.flip();
                deviceCreationInfo.ppEnabledLayerNames(layersptr);
            }
            if(enabledExtensions == null) {
                enabledExtensions = new ArrayList<>();
            }
            if(!enabledExtensions.contains(VK_KHR_SWAPCHAIN_EXTENSION_NAME)) {
                enabledExtensions.add(VK_KHR_SWAPCHAIN_EXTENSION_NAME);
            }
            Predicate<String> predicate = o -> {
                boolean flag = false;
                if(layers != null) {
                    flag = layers.stream().anyMatch(layer -> isExtensionSupported(o, layer));
                }
                return flag || isExtensionSupported(o, null);
            };
            var extptr = stack.mallocPointer(enabledExtensions.size());
            for (String enabledExtension : enabledExtensions) {
                if(predicate.test(enabledExtension))
                extptr.put(MemoryStack.stackUTF8(enabledExtension));
            }
            extptr.flip();
            deviceCreationInfo.ppEnabledExtensionNames(extptr);
            var ptr = stack.mallocPointer(1);
            var err = VK10.vkCreateDevice(vk, deviceCreationInfo, null, ptr);
            if (err != VK_SUCCESS) {
                throw new IllegalStateException("Failed to create device: " + VulkanUtils.translateVulkanResult(err));
            }
            var device = new VkDevice(ptr.get(0), vk, deviceCreationInfo);
            return new LogicalDevice(this, device);
        }
    }

    public DeviceMemoryProperties getMemoryProperties(){
        try(var stack = MemoryStack.stackPush()){
            VkPhysicalDeviceMemoryProperties properties = VkPhysicalDeviceMemoryProperties.callocStack(stack);
            vkGetPhysicalDeviceMemoryProperties(vk, properties);
            return DeviceMemoryProperties.fromNative(properties);
        }
    }
}
