package com.yakimaguro.romaji2kanji;

import com.yakimaguro.romaji2kanji.utils.IMEConverter;
import com.yakimaguro.romaji2kanji.utils.YukiKanaConverter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RomajiMod implements ModInitializer {

    private static final Pattern ASCII_ONLY = Pattern.compile("^\\p{ASCII}+$");

    @Override
    public void onInitialize() {
        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE,
                (ServerPlayerEntity sender, Text message) -> {
                    String original = message.getString();

                    if (ASCII_ONLY.matcher(original).matches()) {
                        String converted = IMEConverter.convByGoogleIME(YukiKanaConverter.conv(original));
                        return Text.literal(original + " -> " + converted);
                    } else {
                        return message;
                    }
        });
    }
}
