
package app.homsai.engine.homeassistant.gateways.dto.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "editable",
    "id",
    "latitude",
    "longitude",
    "gps_accuracy",
    "source",
    "user_id",
    "friendly_name",
    "auto_update",
    "installed_version",
    "in_progress",
    "latest_version",
    "release_summary",
    "release_url",
    "skipped_version",
    "title",
    "entity_picture",
    "supported_features",
    "next_dawn",
    "next_dusk",
    "next_midnight",
    "next_noon",
    "next_rising",
    "next_setting",
    "elevation",
    "azimuth",
    "rising",
    "radius",
    "passive",
    "icon",
    "supported_color_modes",
    "unit_of_measurement",
    "last_triggered",
    "mode",
    "current",
    "attribution",
    "state_class",
    "device_class",
    "source_type",
    "altitude",
    "course",
    "speed",
    "vertical_accuracy",
    "temperature",
    "humidity",
    "pressure",
    "wind_bearing",
    "wind_speed",
    "forecast",
    "repositories",
    "termination",
    "time",
    "setting",
    "hvac_modes",
    "min_temp",
    "max_temp",
    "target_temp_step",
    "preset_modes",
    "current_temperature",
    "current_humidity",
    "hvac_action",
    "preset_mode",
    "offset_celsius",
    "offset_fahrenheit",
    "default_overlay_type",
    "default_overlay_seconds",
    "name",
    "peak_power",
    "last_update_time",
    "installation_date",
    "type",
    "manufacturer_name",
    "model_name",
    "maximum_power",
    "temperature_coef"
})
@Generated("jsonschema2pojo")
public class HomeAssistantAttributesDto {

    @JsonProperty("editable")
    private String editable;
    @JsonProperty("id")
    private String id;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("gps_accuracy")
    private String gpsAccuracy;
    @JsonProperty("source")
    private String source;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("friendly_name")
    private String friendlyName;
    @JsonProperty("auto_update")
    private String autoUpdate;
    @JsonProperty("installed_version")
    private String installedVersion;
    @JsonProperty("in_progress")
    private String inProgress;
    @JsonProperty("latest_version")
    private String latestVersion;
    @JsonProperty("release_summary")
    private Object releaseSummary;
    @JsonProperty("release_url")
    private Object releaseUrl;
    @JsonProperty("skipped_version")
    private Object skippedVersion;
    @JsonProperty("title")
    private String title;
    @JsonProperty("entity_picture")
    private String entityPicture;
    @JsonProperty("supported_features")
    private String supportedFeatures;
    @JsonProperty("next_dawn")
    private String nextDawn;
    @JsonProperty("next_dusk")
    private String nextDusk;
    @JsonProperty("next_midnight")
    private String nextMidnight;
    @JsonProperty("next_noon")
    private String nextNoon;
    @JsonProperty("next_rising")
    private String nextRising;
    @JsonProperty("next_setting")
    private String nextSetting;
    @JsonProperty("elevation")
    private String elevation;
    @JsonProperty("azimuth")
    private String azimuth;
    @JsonProperty("rising")
    private String rising;
    @JsonProperty("radius")
    private String radius;
    @JsonProperty("passive")
    private String passive;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("supported_color_modes")
    private List<String> supportedColorModes = null;
    @JsonProperty("unit_of_measurement")
    private String unitOfMeasurement;
    @JsonProperty("last_triggered")
    private Object lastTriggered;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("current")
    private String current;
    @JsonProperty("attribution")
    private String attribution;
    @JsonProperty("state_class")
    private String stateClass;
    @JsonProperty("device_class")
    private String deviceClass;
    @JsonProperty("source_type")
    private String sourceType;
    @JsonProperty("altitude")
    private String altitude;
    @JsonProperty("course")
    private String course;
    @JsonProperty("speed")
    private String speed;
    @JsonProperty("vertical_accuracy")
    private String verticalAccuracy;
    @JsonProperty("temperature")
    private String temperature;
    @JsonProperty("humidity")
    private String humidity;
    @JsonProperty("pressure")
    private String pressure;
    @JsonProperty("wind_bearing")
    private String windBearing;
    @JsonProperty("wind_speed")
    private String windSpeed;
    @JsonProperty("forecast")
    private List<HomeAssistantForecastDto> homeAssistantForecastDto = null;
    @JsonProperty("repositories")
    private List<HomeAssistantRepositoryDto> repositories = null;
    @JsonProperty("termination")
    private String termination;
    @JsonProperty("time")
    private String time;
    @JsonProperty("setting")
    private String setting;
    @JsonProperty("hvac_modes")
    private List<String> hvacModes = null;
    @JsonProperty("min_temp")
    private String minTemp;
    @JsonProperty("max_temp")
    private String maxTemp;
    @JsonProperty("target_temp_step")
    private String targetTempStep;
    @JsonProperty("preset_modes")
    private List<String> presetModes = null;
    @JsonProperty("current_temperature")
    private String currentTemperature;
    @JsonProperty("current_humidity")
    private String currentHumidity;
    @JsonProperty("hvac_action")
    private String hvacAction;
    @JsonProperty("preset_mode")
    private String presetMode;
    @JsonProperty("offset_celsius")
    private String offsetCelsius;
    @JsonProperty("offset_fahrenheit")
    private String offsetFahrenheit;
    @JsonProperty("default_overlay_type")
    private String defaultOverlayType;
    @JsonProperty("default_overlay_seconds")
    private Object defaultOverlaySeconds;
    @JsonProperty("name")
    private String name;
    @JsonProperty("peak_power")
    private String peakPower;
    @JsonProperty("last_update_time")
    private String lastUpdateTime;
    @JsonProperty("installation_date")
    private String installationDate;
    @JsonProperty("type")
    private String type;
    @JsonProperty("manufacturer_name")
    private String manufacturerName;
    @JsonProperty("model_name")
    private String modelName;
    @JsonProperty("maximum_power")
    private String maximumPower;
    @JsonProperty("temperature_coef")
    private String temperatureCoef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("editable")
    public String getEditable() {
        return editable;
    }

    @JsonProperty("editable")
    public void setEditable(String editable) {
        this.editable = editable;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("gps_accuracy")
    public String getGpsAccuracy() {
        return gpsAccuracy;
    }

    @JsonProperty("gps_accuracy")
    public void setGpsAccuracy(String gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("friendly_name")
    public String getFriendlyName() {
        return friendlyName;
    }

    @JsonProperty("friendly_name")
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @JsonProperty("auto_update")
    public String getAutoUpdate() {
        return autoUpdate;
    }

    @JsonProperty("auto_update")
    public void setAutoUpdate(String autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    @JsonProperty("installed_version")
    public String getInstalledVersion() {
        return installedVersion;
    }

    @JsonProperty("installed_version")
    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    @JsonProperty("in_progress")
    public String getInProgress() {
        return inProgress;
    }

    @JsonProperty("in_progress")
    public void setInProgress(String inProgress) {
        this.inProgress = inProgress;
    }

    @JsonProperty("latest_version")
    public String getLatestVersion() {
        return latestVersion;
    }

    @JsonProperty("latest_version")
    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    @JsonProperty("release_summary")
    public Object getReleaseSummary() {
        return releaseSummary;
    }

    @JsonProperty("release_summary")
    public void setReleaseSummary(Object releaseSummary) {
        this.releaseSummary = releaseSummary;
    }

    @JsonProperty("release_url")
    public Object getReleaseUrl() {
        return releaseUrl;
    }

    @JsonProperty("release_url")
    public void setReleaseUrl(Object releaseUrl) {
        this.releaseUrl = releaseUrl;
    }

    @JsonProperty("skipped_version")
    public Object getSkippedVersion() {
        return skippedVersion;
    }

    @JsonProperty("skipped_version")
    public void setSkippedVersion(Object skippedVersion) {
        this.skippedVersion = skippedVersion;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("entity_picture")
    public String getEntityPicture() {
        return entityPicture;
    }

    @JsonProperty("entity_picture")
    public void setEntityPicture(String entityPicture) {
        this.entityPicture = entityPicture;
    }

    @JsonProperty("supported_features")
    public String getSupportedFeatures() {
        return supportedFeatures;
    }

    @JsonProperty("supported_features")
    public void setSupportedFeatures(String supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    @JsonProperty("next_dawn")
    public String getNextDawn() {
        return nextDawn;
    }

    @JsonProperty("next_dawn")
    public void setNextDawn(String nextDawn) {
        this.nextDawn = nextDawn;
    }

    @JsonProperty("next_dusk")
    public String getNextDusk() {
        return nextDusk;
    }

    @JsonProperty("next_dusk")
    public void setNextDusk(String nextDusk) {
        this.nextDusk = nextDusk;
    }

    @JsonProperty("next_midnight")
    public String getNextMidnight() {
        return nextMidnight;
    }

    @JsonProperty("next_midnight")
    public void setNextMidnight(String nextMidnight) {
        this.nextMidnight = nextMidnight;
    }

    @JsonProperty("next_noon")
    public String getNextNoon() {
        return nextNoon;
    }

    @JsonProperty("next_noon")
    public void setNextNoon(String nextNoon) {
        this.nextNoon = nextNoon;
    }

    @JsonProperty("next_rising")
    public String getNextRising() {
        return nextRising;
    }

    @JsonProperty("next_rising")
    public void setNextRising(String nextRising) {
        this.nextRising = nextRising;
    }

    @JsonProperty("next_setting")
    public String getNextSetting() {
        return nextSetting;
    }

    @JsonProperty("next_setting")
    public void setNextSetting(String nextSetting) {
        this.nextSetting = nextSetting;
    }

    @JsonProperty("elevation")
    public String getElevation() {
        return elevation;
    }

    @JsonProperty("elevation")
    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    @JsonProperty("azimuth")
    public String getAzimuth() {
        return azimuth;
    }

    @JsonProperty("azimuth")
    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    @JsonProperty("rising")
    public String getRising() {
        return rising;
    }

    @JsonProperty("rising")
    public void setRising(String rising) {
        this.rising = rising;
    }

    @JsonProperty("radius")
    public String getRadius() {
        return radius;
    }

    @JsonProperty("radius")
    public void setRadius(String radius) {
        this.radius = radius;
    }

    @JsonProperty("passive")
    public String getPassive() {
        return passive;
    }

    @JsonProperty("passive")
    public void setPassive(String passive) {
        this.passive = passive;
    }

    @JsonProperty("icon")
    public String getIcon() {
        return icon;
    }

    @JsonProperty("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonProperty("supported_color_modes")
    public List<String> getSupportedColorModes() {
        return supportedColorModes;
    }

    @JsonProperty("supported_color_modes")
    public void setSupportedColorModes(List<String> supportedColorModes) {
        this.supportedColorModes = supportedColorModes;
    }

    @JsonProperty("unit_of_measurement")
    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    @JsonProperty("unit_of_measurement")
    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    @JsonProperty("last_triggered")
    public Object getLastTriggered() {
        return lastTriggered;
    }

    @JsonProperty("last_triggered")
    public void setLastTriggered(Object lastTriggered) {
        this.lastTriggered = lastTriggered;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("current")
    public String getCurrent() {
        return current;
    }

    @JsonProperty("current")
    public void setCurrent(String current) {
        this.current = current;
    }

    @JsonProperty("attribution")
    public String getAttribution() {
        return attribution;
    }

    @JsonProperty("attribution")
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    @JsonProperty("state_class")
    public String getStateClass() {
        return stateClass;
    }

    @JsonProperty("state_class")
    public void setStateClass(String stateClass) {
        this.stateClass = stateClass;
    }

    @JsonProperty("device_class")
    public String getDeviceClass() {
        return deviceClass;
    }

    @JsonProperty("device_class")
    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }

    @JsonProperty("source_type")
    public String getSourceType() {
        return sourceType;
    }

    @JsonProperty("source_type")
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @JsonProperty("altitude")
    public String getAltitude() {
        return altitude;
    }

    @JsonProperty("altitude")
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    @JsonProperty("course")
    public String getCourse() {
        return course;
    }

    @JsonProperty("course")
    public void setCourse(String course) {
        this.course = course;
    }

    @JsonProperty("speed")
    public String getSpeed() {
        return speed;
    }

    @JsonProperty("speed")
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @JsonProperty("vertical_accuracy")
    public String getVerticalAccuracy() {
        return verticalAccuracy;
    }

    @JsonProperty("vertical_accuracy")
    public void setVerticalAccuracy(String verticalAccuracy) {
        this.verticalAccuracy = verticalAccuracy;
    }

    @JsonProperty("temperature")
    public String getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("humidity")
    public String getHumidity() {
        return humidity;
    }

    @JsonProperty("humidity")
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    @JsonProperty("pressure")
    public String getPressure() {
        return pressure;
    }

    @JsonProperty("pressure")
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    @JsonProperty("wind_bearing")
    public String getWindBearing() {
        return windBearing;
    }

    @JsonProperty("wind_bearing")
    public void setWindBearing(String windBearing) {
        this.windBearing = windBearing;
    }

    @JsonProperty("wind_speed")
    public String getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed")
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    @JsonProperty("forecast")
    public List<HomeAssistantForecastDto> getForecast() {
        return homeAssistantForecastDto;
    }

    @JsonProperty("forecast")
    public void setForecast(List<HomeAssistantForecastDto> homeAssistantForecastDto) {
        this.homeAssistantForecastDto = homeAssistantForecastDto;
    }

    @JsonProperty("repositories")
    public List<HomeAssistantRepositoryDto> getRepositories() {
        return repositories;
    }

    @JsonProperty("repositories")
    public void setRepositories(List<HomeAssistantRepositoryDto> repositories) {
        this.repositories = repositories;
    }

    @JsonProperty("termination")
    public String getTermination() {
        return termination;
    }

    @JsonProperty("termination")
    public void setTermination(String termination) {
        this.termination = termination;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("setting")
    public String getSetting() {
        return setting;
    }

    @JsonProperty("setting")
    public void setSetting(String setting) {
        this.setting = setting;
    }

    @JsonProperty("hvac_modes")
    public List<String> getHvacModes() {
        return hvacModes;
    }

    @JsonProperty("hvac_modes")
    public void setHvacModes(List<String> hvacModes) {
        this.hvacModes = hvacModes;
    }

    @JsonProperty("min_temp")
    public String getMinTemp() {
        return minTemp;
    }

    @JsonProperty("min_temp")
    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    @JsonProperty("max_temp")
    public String getMaxTemp() {
        return maxTemp;
    }

    @JsonProperty("max_temp")
    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    @JsonProperty("target_temp_step")
    public String getTargetTempStep() {
        return targetTempStep;
    }

    @JsonProperty("target_temp_step")
    public void setTargetTempStep(String targetTempStep) {
        this.targetTempStep = targetTempStep;
    }

    @JsonProperty("preset_modes")
    public List<String> getPresetModes() {
        return presetModes;
    }

    @JsonProperty("preset_modes")
    public void setPresetModes(List<String> presetModes) {
        this.presetModes = presetModes;
    }

    @JsonProperty("current_temperature")
    public String getCurrentTemperature() {
        return currentTemperature;
    }

    @JsonProperty("current_temperature")
    public void setCurrentTemperature(String currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    @JsonProperty("current_humidity")
    public String getCurrentHumidity() {
        return currentHumidity;
    }

    @JsonProperty("current_humidity")
    public void setCurrentHumidity(String currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    @JsonProperty("hvac_action")
    public String getHvacAction() {
        return hvacAction;
    }

    @JsonProperty("hvac_action")
    public void setHvacAction(String hvacAction) {
        this.hvacAction = hvacAction;
    }

    @JsonProperty("preset_mode")
    public String getPresetMode() {
        return presetMode;
    }

    @JsonProperty("preset_mode")
    public void setPresetMode(String presetMode) {
        this.presetMode = presetMode;
    }

    @JsonProperty("offset_celsius")
    public String getOffsetCelsius() {
        return offsetCelsius;
    }

    @JsonProperty("offset_celsius")
    public void setOffsetCelsius(String offsetCelsius) {
        this.offsetCelsius = offsetCelsius;
    }

    @JsonProperty("offset_fahrenheit")
    public String getOffsetFahrenheit() {
        return offsetFahrenheit;
    }

    @JsonProperty("offset_fahrenheit")
    public void setOffsetFahrenheit(String offsetFahrenheit) {
        this.offsetFahrenheit = offsetFahrenheit;
    }

    @JsonProperty("default_overlay_type")
    public String getDefaultOverlayType() {
        return defaultOverlayType;
    }

    @JsonProperty("default_overlay_type")
    public void setDefaultOverlayType(String defaultOverlayType) {
        this.defaultOverlayType = defaultOverlayType;
    }

    @JsonProperty("default_overlay_seconds")
    public Object getDefaultOverlaySeconds() {
        return defaultOverlaySeconds;
    }

    @JsonProperty("default_overlay_seconds")
    public void setDefaultOverlaySeconds(Object defaultOverlaySeconds) {
        this.defaultOverlaySeconds = defaultOverlaySeconds;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("peak_power")
    public String getPeakPower() {
        return peakPower;
    }

    @JsonProperty("peak_power")
    public void setPeakPower(String peakPower) {
        this.peakPower = peakPower;
    }

    @JsonProperty("last_update_time")
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    @JsonProperty("last_update_time")
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @JsonProperty("installation_date")
    public String getInstallationDate() {
        return installationDate;
    }

    @JsonProperty("installation_date")
    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("manufacturer_name")
    public String getManufacturerName() {
        return manufacturerName;
    }

    @JsonProperty("manufacturer_name")
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    @JsonProperty("model_name")
    public String getModelName() {
        return modelName;
    }

    @JsonProperty("model_name")
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @JsonProperty("maximum_power")
    public String getMaximumPower() {
        return maximumPower;
    }

    @JsonProperty("maximum_power")
    public void setMaximumPower(String maximumPower) {
        this.maximumPower = maximumPower;
    }

    @JsonProperty("temperature_coef")
    public String getTemperatureCoef() {
        return temperatureCoef;
    }

    @JsonProperty("temperature_coef")
    public void setTemperatureCoef(String temperatureCoef) {
        this.temperatureCoef = temperatureCoef;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
