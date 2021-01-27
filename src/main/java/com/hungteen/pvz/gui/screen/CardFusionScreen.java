package com.hungteen.pvz.gui.screen;

import java.util.Arrays;
import java.util.List;

import com.hungteen.pvz.gui.GuiHandler;
import com.hungteen.pvz.gui.container.CardFusionContainer;
import com.hungteen.pvz.gui.search.SearchCategories;
import com.hungteen.pvz.gui.search.SearchOption;
import com.hungteen.pvz.network.ClickButtonPacket;
import com.hungteen.pvz.network.PVZPacketHandler;
import com.hungteen.pvz.tileentity.CardFusionTileEntity;
import com.hungteen.pvz.utils.RenderUtil;
import com.hungteen.pvz.utils.StringUtil;
import com.hungteen.pvz.utils.enums.Colors;
import com.hungteen.pvz.utils.enums.Plants;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CardFusionScreen extends AbstractOptionScreen<CardFusionContainer> {

	private static final ResourceLocation TEXTURE = StringUtil.prefix("textures/gui/container/card_fusion.png");
	protected Button craftButton;
	
	public CardFusionScreen(CardFusionContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.xSize = 240;
		this.ySize = 256;
	}

	@Override
	protected void init() {
		super.init();
		this.craftButton = this.addButton(new Button(this.guiLeft + 216, this.guiTop + 154, 26, 18, new TranslationTextComponent("gui.pvz.fragment_splice").getFormattedText(), (button) -> {
			if(this.craftButton.visible) {
			    PVZPacketHandler.CHANNEL.sendToServer(new ClickButtonPacket(GuiHandler.CARD_FUSION, 0, this.container.te.array.get(1)));
			}
		}));
		this.craftButton.visible = false;
	}

	@Override
	public void tick() {
		super.tick();
		if(this.searchGui.getCurrentOption().isPresent()) {
			this.searchGui.getRecipeManager().clear();
			PVZPacketHandler.CHANNEL.sendToServer(new ClickButtonPacket(GuiHandler.FRAGMENT_SPLICE, 1, this.container.te.array.get(1)));
			this.setGhostRecipe(this.searchGui.getCurrentOption().get());
			this.searchGui.resetCurrentOption();
		}
	}
	
	private void setGhostRecipe(SearchOption option) {
		if(option.isPlant()) {
			Plants plantType = option.getPlant().get();
			this.searchGui.getRecipeManager().setRecipe(this.container.getRecipeForPlant(plantType));
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.craftButton.visible = this.canCraftNow();
		this.renderHoveredToolTip(mouseX, mouseY);
		StringUtil.drawCenteredScaledString(font, new TranslationTextComponent("block.pvz.card_fusion").getFormattedText(), this.guiLeft + this.xSize / 2, this.guiTop + 8, Colors.BLACK, 1F);
		StringUtil.drawCenteredScaledString(font, "" + this.container.te.array.get(0), this.guiLeft + 15, this.guiTop + 67, Colors.BLACK, 0.5F);
	    super.render(mouseX, mouseY, partialTicks);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		int maxLen = 124;
		int len = RenderUtil.getRenderBarLen(this.container.te.array.get(0), CardFusionTileEntity.MAX_SUN_AMOUNT, maxLen);
		blit(this.guiLeft + 7, this.guiTop + 149 - len + 1, 240, 0, 16, len);
		RenderSystem.popMatrix();
	}

	protected boolean canCraftNow() {
		return this.container.te.array.get(1) >= 0;
	}
	
	@Override
	public boolean isOptionUnLocked(SearchOption option) {
		return true;
	}

	@Override
	public List<SearchCategories> getSearchCategories() {
		return Arrays.asList(SearchCategories.FUSION);
	}

}