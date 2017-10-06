package com.dhdigital.lms.modal;

/**
 * Created by Parveen on 9/6/2016.
 */
public class TenantConfiguration {

    private int id;
    private BrandingConfiguration brandingConfiguration;
    private String configurationLevel;
    private String shortDateFormat;
    private String longDateFormat;
    private int outPolicyLevels;
    private boolean travelReservationRequired;
    private MasterData baseCurrency;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfigurationLevel() {
        return configurationLevel;
    }

    public void setConfigurationLevel(String configurationLevel) {
        this.configurationLevel = configurationLevel;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }


    public int getOutPolicyLevels() {
        return outPolicyLevels;
    }

    public void setOutPolicyLevels(int outPolicyLevels) {
        this.outPolicyLevels = outPolicyLevels;
    }

    public boolean isTravelReservationRequired() {
        return travelReservationRequired;
    }

    public void setTravelReservationRequired(boolean travelReservationRequired) {
        this.travelReservationRequired = travelReservationRequired;
    }

    public BrandingConfiguration getBrandingConfiguration() {
        return brandingConfiguration;
    }

    public void setBrandingConfiguration(BrandingConfiguration brandingConfiguration) {
        this.brandingConfiguration = brandingConfiguration;
    }

    public MasterData getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(MasterData baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
