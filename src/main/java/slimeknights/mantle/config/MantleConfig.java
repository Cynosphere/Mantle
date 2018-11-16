package slimeknights.mantle.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import slimeknights.mantle.pulsar.config.ForgeCFG;
import slimeknights.mantle.Mantle;

public final class MantleConfig {
    public static ForgeCFG pulseConfig = new ForgeCFG("MantleModules","Modules");
    public static MantleConfig instance = new MantleConfig();

    private MantleConfig() {
    }

    public static boolean heartsEnabled = false;

    static Configuration configFile;

    static ConfigCategory Modules;
    static ConfigCategory Gameplay;
    static ConfigCategory Worldgen;
    static ConfigCategory ClientSide;

    public static void load(FMLPreInitializationEvent event) {
        configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.1", false);
        MinecraftForge.EVENT_BUS.register(instance);
        syncConfig();
    }

    @SubscribeEvent
    public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(Mantle.modId)) {
            syncConfig();
        }
    }


    public static boolean syncConfig() {
        Property prop;

        {
            Modules = pulseConfig.getCategory();
        }

        {
            String cat = "clientside";
            List<String> propOrder = Lists.newArrayList();
            Gameplay = configFile.getCategory(cat);

            prop = configFile.get(cat, "heartsEnabled", heartsEnabled);
            prop.setComment("Enable or disable Mantle's heart rendering.");
            heartsEnabled = prop.getBoolean();
            propOrder.add(prop.getName());
        }

        boolean changed = false;
        if(configFile.hasChanged()) {
            configFile.save();
            changed = true;
        }
        if(pulseConfig.getConfig().hasChanged()) {
            pulseConfig.flush();
            changed = true;
        }
        return changed;
    }
}