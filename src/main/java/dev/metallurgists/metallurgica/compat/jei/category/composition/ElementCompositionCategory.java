package dev.metallurgists.metallurgica.compat.jei.category.composition;

import dev.metallurgists.metallurgica.compat.jei.MetallurgicaJeiConstants;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.MaterialCompositionManager;
import dev.metallurgists.metallurgica.infastructure.element.Element;
import dev.metallurgists.metallurgica.infastructure.element.data.ElementData;
import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaElements;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.CreateLang;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.createmod.catnip.layout.LayoutHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ElementCompositionCategory extends CreateRecipeCategory<ElementCompositionRecipe> {
    public static final RecipeType<ElementCompositionRecipe> ITEM_COMPOSITION = RecipeType.create("metallurgica", "element_composition", ElementCompositionRecipe.class);
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public static final List<ElementCompositionRecipe> COMPOSITIONS = new ArrayList<>();

    static {
        CompositionManager.compositions.forEach((item, composition) -> {
            COMPOSITIONS.add(ElementCompositionRecipe.create(composition.compositions(), composition.item(), null));
        });
        MaterialCompositionManager.materialCompositions.forEach((material, composition) -> {
            COMPOSITIONS.add(ElementCompositionRecipe.create(composition.compositions(), null, material));
        });
    }

    public ElementCompositionCategory(Info<ElementCompositionRecipe> info) {
        super(info);
        this.icon = asDrawable(AllGuiTextures.JEI_QUESTION_MARK);
        this.slotBackground = asDrawable(AllGuiTextures.JEI_SLOT);
    }

    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ITEM_COMPOSITION, COMPOSITIONS);
    }

    @Override
    public RecipeType<ElementCompositionRecipe> getRecipeType() {
        return ITEM_COMPOSITION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("metallurgica.jei.category.element_composition");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElementCompositionRecipe recipe, IFocusGroup iFocusGroup) {
        int xPos = 72;
        if (recipe.getItem() != null) {
            layoutItemFluidOutput(new ItemStack(recipe.getItem())).forEach(layoutEntry -> {
                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, (getBackground().getWidth() / 2) + layoutEntry.posX() + 1, 1).setBackground(slotBackground, -1, -1);
                if (layoutEntry.item != null) {
                    addIngredient(layoutEntry.item.getItem(), slotBuilder);
                }
                if (layoutEntry.fluid != null) {
                    slotBuilder.addFluidStack(layoutEntry.fluid.getFluid(), 1000);
                }
            });
        } else {
            layoutMaterialOutput(recipe.getAllMaterialItems()).forEach(layoutEntry -> {
                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, (getBackground().getWidth() / 2) + layoutEntry.posX() + 1, 1).setBackground(slotBackground, -1, -1);
                if (!layoutEntry.items.isEmpty()) {
                    addListedOutput(layoutEntry.items, slotBuilder);
                }
            });
        }

        addElements(recipe.getSubCompositions(), builder);

    }

    private static <T> void addIngredient(Item item, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addItemStack(item.getDefaultInstance());
    }

    private <T> void addElements(List<SubComposition> subCompositions, IRecipeLayoutBuilder builder) {
        Map<Element, Integer> elementCounts = new HashMap<>();
        int totalElementsAmount = 0;
        for (SubComposition subComposition : subCompositions) {
            for (ElementData elementData : subComposition.getElements()) {
                Element element = MetallurgicaRegistries.BasicRegistries.registeredElements.getOrDefault(elementData.element(), MetallurgicaElements.NULL.get());
                int amount = elementData.amount();
                elementCounts.put(element, elementCounts.getOrDefault(element, 0) + amount);
                totalElementsAmount += amount;
            }
        }
        int xOffset = getBackground().getWidth() / 2;
        int yOffset = 18 * 2;
        layoutOutput(elementCounts, totalElementsAmount).forEach(layoutEntry -> builder
                .addSlot(RecipeIngredientRole.OUTPUT, (xOffset) + layoutEntry.posX() + 1, yOffset + layoutEntry.posY() + 1)
                .setBackground(asDrawable(AllGuiTextures.JEI_SLOT), -1, -1)
                .addIngredient(MetallurgicaJeiConstants.ELEMENT, layoutEntry.output)
                .addRichTooltipCallback(addPercentageTooltipCallback(layoutEntry.percentage))
        );
    }

    private List<LayoutEntry> layoutOutput(Map<Element, Integer> elements, int totalElementsAmount) {
        int size = elements.size();
        List<LayoutEntry> positions = new ArrayList<>(size);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
        for (Map.Entry<Element, Integer> element : elements.entrySet()) {
            float percentage = (float) element.getValue() / totalElementsAmount;
            positions.add(new LayoutEntry(element.getKey(), percentage, layout.getX(), layout.getY()));
            layout.next();
        }

        return positions;
    }

    private List<ItemFluidLayoutEntry> layoutItemFluidOutput(ItemStack item) {
        int size = 1;
        FluidStack fluid = item.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(handler -> {
            if (handler.getFluidInTank(0).isEmpty()) {
                return null;
            }
            return handler.getFluidInTank(0);
        }).orElse(null);
        if (fluid != null) size = 2;
        List<ItemFluidLayoutEntry> positions = new ArrayList<>(size);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
        if (!item.isEmpty()) {
            positions.add(new ItemFluidLayoutEntry(item, null, layout.getX(), layout.getY()));
            layout.next();
        }
        if (fluid != null && !fluid.isEmpty()) {
            positions.add(new ItemFluidLayoutEntry(null, fluid, layout.getX(), layout.getY()));
            layout.next();
        }
        return positions;
    }

    private List<MaterialLayoutEntry> layoutMaterialOutput(List<ItemStack> items) {
        int size = 1;
        List<MaterialLayoutEntry> positions = new ArrayList<>(size);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 0);
        positions.add(new MaterialLayoutEntry(items, layout.getX(), layout.getY()));
        layout.next();
        return positions;
    }

    public static IRecipeSlotRichTooltipCallback addPercentageTooltipCallback(float percentage) {
        return (view, tooltip) -> {
            if (percentage != 1) {
                var percentageStr = percentage < 0.01 ? "<1" : (int) (percentage * 100);
                tooltip.add(CreateLang.text(percentageStr + "%").component()
                        .withStyle(ChatFormatting.GOLD));
            }

        };
    }

    private record LayoutEntry(
            Element output,
            float percentage,
            int posX,
            int posY
    ) {}

    private record ItemFluidLayoutEntry(
            @Nullable ItemStack item,
            @Nullable FluidStack fluid,
            int posX,
            int posY
    ) {}

    private record MaterialLayoutEntry(
            List<ItemStack> items,
            int posX,
            int posY
    ) {}

    private static <T> void addListedOutput(List<ItemStack> stacks, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addItemStacks(stacks);
    }

    protected static IDrawable asDrawable(final AllGuiTextures texture) {
        return new IDrawable() {
            public int getWidth() {
                return texture.getWidth();
            }

            public int getHeight() {
                return texture.getHeight();
            }

            @Override
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                texture.render(graphics, xOffset, yOffset);
            }
        };
    }
}
