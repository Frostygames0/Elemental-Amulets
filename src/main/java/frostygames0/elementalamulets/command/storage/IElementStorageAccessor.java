package frostygames0.elementalamulets.command.storage;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.network.chat.Component;

public interface IElementStorageAccessor {
    IElementStorage getStorage() throws CommandSyntaxException;

    Component getSuccessMessage(Component actionSubMessage);

    Component getFailureMessage(Component actionSubMessage);

    Component getNeutralMessage(Component actionSubMessage);
}
