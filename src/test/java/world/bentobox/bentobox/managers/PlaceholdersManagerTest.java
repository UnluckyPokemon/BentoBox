package world.bentobox.bentobox.managers;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.addons.GameModeAddon;

import static org.mockito.Mockito.when;

/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( {BentoBox.class} )
public class PlaceholdersManagerTest {

    @Mock
    private BentoBox plugin;
    @Mock
    private GameModeAddon addon;
    @Mock
    private PlaceholdersManager pm;

    @Before
    public void setUp() throws Exception {
        // Addon
        @NonNull
        AddonDescription desc = new AddonDescription.Builder("main", "bskyblock", "1.0").build();
        when(addon.getDescription()).thenReturn(desc);

        when(plugin.getPlaceholdersManager()).thenReturn(pm);
        // No placeholders registered yet
        when(pm.isPlaceholder(Mockito.any(), Mockito.any())).thenReturn(false);
    }

    /**
     * Test method for {@link world.bentobox.bentobox.managers.PlaceholdersManager#registerDefaultPlaceholders(GameModeAddon)}.
     */
    @Test
    public void testRegisterGameModePlaceholdersAllDefaults() {
        pm.registerDefaultPlaceholders(addon);
        // 7 registrations for this addon
        Mockito.verify(pm, Mockito.atLeast(1)).registerPlaceholder(Mockito.anyString(), Mockito.any());
    }

    /**
     * Test method for {@link world.bentobox.bentobox.managers.PlaceholdersManager#registerDefaultPlaceholders(GameModeAddon)}.
     */
    @Test
    public void testRegisterDefaultPlaceholdersSomePreregistered() {
        // Some duplicates
        when(pm.isPlaceholder(Mockito.any(), Mockito.any())).thenReturn(false, true, true, false, false, true, false);

        pm.registerDefaultPlaceholders(addon);

        // 3 registrations for this addon
        Mockito.verify(pm, Mockito.atLeast(1)).registerPlaceholder(Mockito.anyString(), Mockito.any());
    }
}