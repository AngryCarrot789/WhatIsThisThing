package reghzy.witt.integration;

import net.minecraft.src.TileEntity;
import reghzy.witt.data.ToolTip;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class for allowing additional data to be supplied to a tooltip
 */
public class ToolTipIntegration {
    private static final ToolTipIntegration instance = new ToolTipIntegration();

    private final HashMap<Class<?>, ArrayList<IDataProvider>> tileInfoProvider;

    private ToolTipIntegration() {
        if (instance != null)
            throw new UnsupportedOperationException("What for!!!");

        this.tileInfoProvider = new HashMap<Class<?>, ArrayList<IDataProvider>>();
    }

    public void registerTileProvider(Class<?> klass, IDataProvider provider) {
        ArrayList<IDataProvider> list = this.tileInfoProvider.get(klass);
        if (list == null) {
            this.tileInfoProvider.put(klass, list = new ArrayList<IDataProvider>());
        }
        else if (list.contains(provider)) {
            return;
        }

        list.add(provider);
    }

    public void provideTileInformation(ToolTip tip, TileEntity tileEntity) {
        ArrayList<IDataProvider> list = this.tileInfoProvider.get(tileEntity.getClass());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).provide(tip, tileEntity);
            }
        }
    }

    public static ToolTipIntegration getInstance() {
        return instance;
    }
}
