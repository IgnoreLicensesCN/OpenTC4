package thaumcraft.client.renderers.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;

public class ModelEldritchGuardian extends ModelBase {
   ModelRenderer BeltR;
   ModelRenderer Mbelt;
   ModelRenderer MbeltL;
   ModelRenderer MbeltR;
   ModelRenderer BeltL;
   ModelRenderer Hood4;
   ModelRenderer Cloak3;
   ModelRenderer Chestplate;
   ModelRenderer HoodEye;
   ModelRenderer Hood1;
   ModelRenderer Hood2;
   ModelRenderer Hood3;
   ModelRenderer Backplate;
   ModelRenderer Cloak1;
   ModelRenderer Cloak2;
   ModelRenderer ShoulderplateTopR;
   ModelRenderer ShoulderplateR1;
   ModelRenderer ShoulderplateR2;
   ModelRenderer ShoulderplateR3;
   ModelRenderer ShoulderR;
   ModelRenderer ArmR3;
   ModelRenderer ArmL1;
   ModelRenderer ArmL3;
   ModelRenderer ArmR1;
   ModelRenderer ArmR2;
   ModelRenderer ArmL2;
   ModelRenderer ShoulderL;
   ModelRenderer ShoulderplateLtop;
   ModelRenderer ShoulderplateL1;
   ModelRenderer ShoulderplateL2;
   ModelRenderer ShoulderplateL3;
   ModelRenderer LegpanelR4;
   ModelRenderer LegpanelR5;
   ModelRenderer LegpanelR6;
   ModelRenderer SidepanelR1;
   ModelRenderer BackpanelR1;
   ModelRenderer BackpanelR2;
   ModelRenderer BackpanelR3;
   ModelRenderer BackpanelL3;
   ModelRenderer LegpanelL4;
   ModelRenderer LegpanelL5;
   ModelRenderer LegpanelL6;
   ModelRenderer SidepanelL1;
   ModelRenderer SidepanelR4;
   ModelRenderer BackpanelL1;
   ModelRenderer BackpanelL2;
   ModelRenderer LegpanelC1;
   ModelRenderer LegpanelC2;
   ModelRenderer LegpanelC3;
   ModelRenderer SidepanelR3;
   ModelRenderer SidepanelL4;
   ModelRenderer SidepanelL3;
   ModelRenderer SidepanelR2;
   ModelRenderer SidepanelL2;
   private float partialTicks;

   public ModelEldritchGuardian() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.BeltR = new ModelRenderer(this, 76, 44);
      this.BeltR.addBox(-5.0F, 4.0F, -3.0F, 1, 3, 6);
      this.BeltR.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.BeltR.setTextureSize(128, 64);
      this.BeltR.mirror = true;
      this.setRotation(this.BeltR, 0.0F, 0.0F, 0.0F);
      this.Mbelt = new ModelRenderer(this, 56, 55);
      this.Mbelt.addBox(-4.0F, 8.0F, -3.0F, 8, 4, 1);
      this.Mbelt.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.Mbelt.setTextureSize(128, 64);
      this.Mbelt.mirror = true;
      this.setRotation(this.Mbelt, 0.0F, 0.0F, 0.0F);
      this.MbeltL = new ModelRenderer(this, 76, 44);
      this.MbeltL.addBox(4.0F, 8.0F, -3.0F, 1, 3, 6);
      this.MbeltL.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.MbeltL.setTextureSize(128, 64);
      this.MbeltL.mirror = true;
      this.setRotation(this.MbeltL, 0.0F, 0.0F, 0.0F);
      this.MbeltR = new ModelRenderer(this, 76, 44);
      this.MbeltR.addBox(-5.0F, 8.0F, -3.0F, 1, 3, 6);
      this.MbeltR.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.MbeltR.setTextureSize(128, 64);
      this.MbeltR.mirror = true;
      this.setRotation(this.MbeltR, 0.0F, 0.0F, 0.0F);
      this.BeltL = new ModelRenderer(this, 76, 44);
      this.BeltL.addBox(4.0F, 4.0F, -3.0F, 1, 3, 6);
      this.BeltL.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.BeltL.setTextureSize(128, 64);
      this.BeltL.mirror = true;
      this.setRotation(this.BeltL, 0.0F, 0.0F, 0.0F);
      this.Chestplate = new ModelRenderer(this, 56, 45);
      this.Chestplate.addBox(-4.0F, 1.0F, -4.0F, 8, 7, 2);
      this.Chestplate.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.Chestplate.setTextureSize(128, 64);
      this.Chestplate.mirror = true;
      this.setRotation(this.Chestplate, 0.0F, 0.0F, 0.0F);
      this.HoodEye = new ModelRenderer(this, 0, 0);
      this.HoodEye.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.HoodEye.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.HoodEye.setTextureSize(128, 64);
      this.HoodEye.mirror = true;
      this.setRotation(this.HoodEye, 0.0F, 0.0F, 0.0F);
      this.Hood1 = new ModelRenderer(this, 40, 12);
      this.Hood1.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.Hood1.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.Hood1.setTextureSize(128, 64);
      this.Hood1.mirror = true;
      this.setRotation(this.Hood1, 0.0F, 0.0F, 0.0F);
      this.Hood2 = new ModelRenderer(this, 36, 28);
      this.Hood2.addBox(-3.5F, -8.7F, 2.0F, 7, 7, 3);
      this.Hood2.setTextureSize(128, 64);
      this.Hood2.mirror = true;
      this.setRotation(this.Hood2, -0.2268928F, 0.0F, 0.0F);
      this.Hood3 = new ModelRenderer(this, 22, 19);
      this.Hood3.addBox(-3.0F, -9.0F, 2.5F, 6, 6, 3);
      this.Hood3.setTextureSize(128, 64);
      this.Hood3.mirror = true;
      this.setRotation(this.Hood3, -0.3490659F, 0.0F, 0.0F);
      this.Hood4 = new ModelRenderer(this, 40, 4);
      this.Hood4.addBox(-2.5F, -9.7F, 3.5F, 5, 5, 3);
      this.Hood4.setTextureSize(128, 64);
      this.Hood4.mirror = true;
      this.setRotation(this.Hood4, -0.5759587F, 0.0F, 0.0F);
      this.Hood1.addChild(this.Hood2);
      this.Hood1.addChild(this.Hood3);
      this.Hood1.addChild(this.Hood4);
      this.Backplate = new ModelRenderer(this, 36, 45);
      this.Backplate.addBox(-4.0F, 1.0F, 2.0F, 8, 11, 2);
      this.Backplate.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.Backplate.setTextureSize(128, 64);
      this.Backplate.mirror = true;
      this.setRotation(this.Backplate, 0.0F, 0.0F, 0.0F);
      this.ShoulderplateTopR = new ModelRenderer(this, 110, 37);
      this.ShoulderplateTopR.addBox(-5.5F, -2.5F, -3.5F, 2, 1, 7);
      this.ShoulderplateTopR.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.ShoulderplateTopR.setTextureSize(128, 64);
      this.ShoulderplateTopR.mirror = true;
      this.setRotation(this.ShoulderplateTopR, -0.3665191F, 0.3141593F, 0.4363323F);
      this.ShoulderplateR1 = new ModelRenderer(this, 110, 45);
      this.ShoulderplateR1.addBox(3.5F, -1.5F, -3.5F, 1, 4, 7);
      this.ShoulderplateR1.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.ShoulderplateR1.setTextureSize(128, 64);
      this.ShoulderplateR1.mirror = true;
      this.setRotation(this.ShoulderplateR1, -0.3665191F, -0.3141593F, -0.4363323F);
      this.ShoulderplateR2 = new ModelRenderer(this, 94, 45);
      this.ShoulderplateR2.addBox(-3.5F, 1.5F, -3.5F, 1, 3, 7);
      this.ShoulderplateR2.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.ShoulderplateR2.setTextureSize(128, 64);
      this.ShoulderplateR2.mirror = true;
      this.setRotation(this.ShoulderplateR2, -0.3665191F, 0.3141593F, 0.4363323F);
      this.ShoulderplateR3 = new ModelRenderer(this, 94, 45);
      this.ShoulderplateR3.addBox(-2.5F, 3.5F, -3.5F, 1, 3, 7);
      this.ShoulderplateR3.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.ShoulderplateR3.setTextureSize(128, 64);
      this.ShoulderplateR3.mirror = true;
      this.setRotation(this.ShoulderplateR3, -0.3665191F, 0.3141593F, 0.4363323F);
      this.ShoulderR = new ModelRenderer(this, 56, 35);
      this.ShoulderR.addBox(-3.5F, -2.5F, -2.5F, 5, 5, 5);
      this.ShoulderR.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.ShoulderR.setTextureSize(128, 64);
      this.ShoulderR.mirror = true;
      this.setRotation(this.ShoulderR, -0.3665191F, 0.122173F, 0.0349066F);
      this.ArmL1 = new ModelRenderer(this, 72, 8);
      this.ArmL1.addBox(-1.0F, 2.5F, -1.5F, 4, 10, 5);
      this.ArmL1.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.ArmL1.setTextureSize(128, 64);
      this.ArmL1.mirror = true;
      this.setRotation(this.ArmL1, -0.9599311F, -0.1047198F, -0.1919862F);
      this.ArmL2 = new ModelRenderer(this, 76, 28);
      this.ArmL2.addBox(-1.0F, 9.5F, 3.5F, 4, 3, 3);
      this.ArmL2.setTextureSize(128, 64);
      this.ArmL2.mirror = true;
      this.ArmL3 = new ModelRenderer(this, 76, 23);
      this.ArmL3.addBox(-1.0F, 6.5F, 3.5F, 4, 3, 2);
      this.ArmL3.setTextureSize(128, 64);
      this.ArmL3.mirror = true;
      this.ArmL1.addChild(this.ArmL2);
      this.ArmL1.addChild(this.ArmL3);
      this.ArmR1 = new ModelRenderer(this, 72, 8);
      this.ArmR1.addBox(-3.0F, 2.5F, -1.5F, 4, 10, 5);
      this.ArmR1.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.ArmR1.setTextureSize(128, 64);
      this.ArmR1.mirror = true;
      this.setRotation(this.ArmR1, -0.9599311F, 0.1047198F, 0.1919862F);
      this.ArmR2 = new ModelRenderer(this, 76, 28);
      this.ArmR2.addBox(-3.0F, 9.5F, 3.5F, 4, 3, 3);
      this.ArmR2.setTextureSize(128, 64);
      this.ArmR2.mirror = true;
      this.ArmR3 = new ModelRenderer(this, 76, 23);
      this.ArmR3.addBox(-3.0F, 6.5F, 3.5F, 4, 3, 2);
      this.ArmR3.setTextureSize(128, 64);
      this.ArmR3.mirror = true;
      this.ArmR1.addChild(this.ArmR2);
      this.ArmR1.addChild(this.ArmR3);
      this.ShoulderL = new ModelRenderer(this, 56, 35);
      this.ShoulderL.mirror = true;
      this.ShoulderL.addBox(-1.5F, -2.5F, -2.5F, 5, 5, 5);
      this.ShoulderL.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.ShoulderL.setTextureSize(128, 64);
      this.setRotation(this.ShoulderL, -0.3665191F, -0.122173F, -0.0349066F);
      this.ShoulderplateLtop = new ModelRenderer(this, 110, 37);
      this.ShoulderplateLtop.addBox(3.5F, -2.5F, -3.5F, 2, 1, 7);
      this.ShoulderplateLtop.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.ShoulderplateLtop.setTextureSize(128, 64);
      this.ShoulderplateLtop.mirror = true;
      this.setRotation(this.ShoulderplateLtop, -0.3665191F, -0.3141593F, -0.4363323F);
      this.ShoulderplateL1 = new ModelRenderer(this, 110, 45);
      this.ShoulderplateL1.addBox(-4.5F, -1.5F, -3.5F, 1, 4, 7);
      this.ShoulderplateL1.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.ShoulderplateL1.setTextureSize(128, 64);
      this.ShoulderplateL1.mirror = true;
      this.setRotation(this.ShoulderplateL1, -0.3665191F, 0.3141593F, 0.4363323F);
      this.ShoulderplateLtop.mirror = false;
      this.ShoulderplateL2 = new ModelRenderer(this, 94, 45);
      this.ShoulderplateL2.addBox(2.5F, 1.5F, -3.5F, 1, 3, 7);
      this.ShoulderplateL2.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.ShoulderplateL2.setTextureSize(128, 64);
      this.ShoulderplateL2.mirror = true;
      this.setRotation(this.ShoulderplateL2, -0.3665191F, -0.3141593F, -0.4363323F);
      this.ShoulderplateL2.mirror = false;
      this.ShoulderplateL3 = new ModelRenderer(this, 94, 45);
      this.ShoulderplateL3.addBox(1.5F, 3.5F, -3.5F, 1, 3, 7);
      this.ShoulderplateL3.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.ShoulderplateL3.setTextureSize(128, 64);
      this.ShoulderplateL3.mirror = true;
      this.setRotation(this.ShoulderplateL3, -0.3665191F, -0.3141593F, -0.4363323F);
      this.ShoulderplateL3.mirror = false;
      this.LegpanelR4 = new ModelRenderer(this, 0, 43);
      this.LegpanelR4.addBox(-3.0F, 0.5F, -3.5F, 2, 3, 1);
      this.LegpanelR4.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.LegpanelR4.setTextureSize(128, 64);
      this.LegpanelR4.mirror = true;
      this.setRotation(this.LegpanelR4, -0.4363323F, 0.0F, 0.0F);
      this.LegpanelR5 = new ModelRenderer(this, 0, 47);
      this.LegpanelR5.addBox(-3.0F, 2.5F, -2.5F, 2, 3, 1);
      this.LegpanelR5.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.LegpanelR5.setTextureSize(128, 64);
      this.LegpanelR5.mirror = true;
      this.setRotation(this.LegpanelR5, -0.4363323F, 0.0F, 0.0F);
      this.LegpanelR6 = new ModelRenderer(this, 6, 43);
      this.LegpanelR6.addBox(-3.0F, 4.5F, -1.5F, 2, 3, 1);
      this.LegpanelR6.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.LegpanelR6.setTextureSize(128, 64);
      this.LegpanelR6.mirror = true;
      this.setRotation(this.LegpanelR6, -0.4363323F, 0.0F, 0.0F);
      this.BackpanelR1 = new ModelRenderer(this, 0, 18);
      this.BackpanelR1.addBox(-3.0F, 0.5F, 2.5F, 5, 3, 1);
      this.BackpanelR1.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.BackpanelR1.setTextureSize(128, 64);
      this.BackpanelR1.mirror = true;
      this.setRotation(this.BackpanelR1, 0.4363323F, 0.0F, 0.0F);
      this.BackpanelR2 = new ModelRenderer(this, 0, 18);
      this.BackpanelR2.addBox(-3.0F, 2.5F, 1.5F, 5, 3, 1);
      this.BackpanelR2.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.BackpanelR2.setTextureSize(128, 64);
      this.BackpanelR2.mirror = true;
      this.setRotation(this.BackpanelR2, 0.4363323F, 0.0F, 0.0F);
      this.BackpanelR3 = new ModelRenderer(this, 0, 18);
      this.BackpanelR3.addBox(-3.0F, 4.5F, 0.5F, 5, 3, 1);
      this.BackpanelR3.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.BackpanelR3.setTextureSize(128, 64);
      this.BackpanelR3.mirror = true;
      this.setRotation(this.BackpanelR3, 0.4363323F, 0.0F, 0.0F);
      this.BackpanelL3 = new ModelRenderer(this, 0, 18);
      this.BackpanelL3.addBox(-2.0F, 4.5F, 0.5F, 5, 3, 1);
      this.BackpanelL3.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.BackpanelL3.setTextureSize(128, 64);
      this.BackpanelL3.mirror = true;
      this.setRotation(this.BackpanelL3, 0.4363323F, 0.0F, 0.0F);
      this.LegpanelL4 = new ModelRenderer(this, 0, 43);
      this.LegpanelL4.addBox(1.0F, 0.5F, -3.5F, 2, 3, 1);
      this.LegpanelL4.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.LegpanelL4.setTextureSize(128, 64);
      this.LegpanelL4.mirror = true;
      this.setRotation(this.LegpanelL4, -0.4363323F, 0.0F, 0.0F);
      this.LegpanelL4.mirror = false;
      this.LegpanelL5 = new ModelRenderer(this, 0, 47);
      this.LegpanelL5.addBox(1.0F, 2.5F, -2.5F, 2, 3, 1);
      this.LegpanelL5.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.LegpanelL5.setTextureSize(128, 64);
      this.LegpanelL5.mirror = true;
      this.setRotation(this.LegpanelL5, -0.4363323F, 0.0F, 0.0F);
      this.LegpanelL5.mirror = false;
      this.LegpanelL6 = new ModelRenderer(this, 6, 43);
      this.LegpanelL6.addBox(1.0F, 4.5F, -1.5F, 2, 3, 1);
      this.LegpanelL6.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.LegpanelL6.setTextureSize(128, 64);
      this.LegpanelL6.mirror = true;
      this.setRotation(this.LegpanelL6, -0.4363323F, 0.0F, 0.0F);
      this.LegpanelL6.mirror = false;
      this.BackpanelL1 = new ModelRenderer(this, 0, 18);
      this.BackpanelL1.addBox(-2.0F, 0.5F, 2.5F, 5, 3, 1);
      this.BackpanelL1.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.BackpanelL1.setTextureSize(128, 64);
      this.BackpanelL1.mirror = true;
      this.setRotation(this.BackpanelL1, 0.4363323F, 0.0F, 0.0F);
      this.BackpanelL2 = new ModelRenderer(this, 0, 18);
      this.BackpanelL2.addBox(-2.0F, 2.5F, 1.5F, 5, 3, 1);
      this.BackpanelL2.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.BackpanelL2.setTextureSize(128, 64);
      this.BackpanelL2.mirror = true;
      this.setRotation(this.BackpanelL2, 0.4363323F, 0.0F, 0.0F);
      this.SidepanelL1 = new ModelRenderer(this, 0, 22);
      this.SidepanelL1.addBox(1.5F, 0.5F, -2.5F, 1, 4, 5);
      this.SidepanelL1.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.SidepanelL1.setTextureSize(128, 64);
      this.SidepanelL1.mirror = true;
      this.setRotation(this.SidepanelL1, 0.0F, 0.0F, -0.4363323F);
      this.SidepanelR1 = new ModelRenderer(this, 0, 22);
      this.SidepanelR1.addBox(-2.5F, 0.5F, -2.5F, 1, 4, 5);
      this.SidepanelR1.setRotationPoint(-2.0F, 6.0F, 0.0F);
      this.SidepanelR1.setTextureSize(128, 64);
      this.SidepanelR1.mirror = true;
      this.setRotation(this.SidepanelR1, 0.0F, 0.0F, 0.4363323F);
      this.SidepanelR2 = new ModelRenderer(this, 0, 54);
      this.SidepanelR2.addBox(0.0F, 0.0F, -0.5F, 1, 5, 5);
      this.SidepanelR2.setRotationPoint(-4.5F, 9.5F, -2.0F);
      this.SidepanelR2.setTextureSize(128, 64);
      this.SidepanelR2.mirror = true;
      this.setRotation(this.SidepanelR2, 0.0F, 0.0F, 0.122173F);
      this.SidepanelR3 = new ModelRenderer(this, 0, 35);
      this.SidepanelR3.addBox(0.0F, 0.0F, -0.5F, 1, 3, 5);
      this.SidepanelR3.setRotationPoint(0.0F, 5.0F, 0.0F);
      this.SidepanelR3.setTextureSize(128, 64);
      this.SidepanelR3.mirror = true;
      this.setRotation(this.SidepanelR3, 0.0F, 0.0F, 0.296706F);
      this.SidepanelR4 = new ModelRenderer(this, 24, 35);
      this.SidepanelR4.addBox(0.0F, 0.0F, -0.5F, 1, 3, 5);
      this.SidepanelR4.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.SidepanelR4.setTextureSize(128, 64);
      this.SidepanelR4.mirror = true;
      this.setRotation(this.SidepanelR4, 0.0F, 0.0F, ((float)Math.PI / 6F));
      this.SidepanelL2 = new ModelRenderer(this, 0, 54);
      this.SidepanelL2.addBox(0.0F, 0.0F, -0.5F, 1, 5, 5);
      this.SidepanelL2.setRotationPoint(4.5F, 9.5F, -2.0F);
      this.SidepanelL2.setTextureSize(128, 64);
      this.SidepanelL2.mirror = true;
      this.setRotation(this.SidepanelL2, 0.0F, 0.0F, -0.122173F);
      this.SidepanelL3 = new ModelRenderer(this, 0, 35);
      this.SidepanelL3.addBox(0.0F, 0.0F, -0.5F, 1, 3, 5);
      this.SidepanelL3.setRotationPoint(0.0F, 5.0F, 0.0F);
      this.SidepanelL3.setTextureSize(128, 64);
      this.SidepanelL3.mirror = true;
      this.setRotation(this.SidepanelL3, 0.0F, 0.0F, -0.296706F);
      this.SidepanelL4 = new ModelRenderer(this, 24, 35);
      this.SidepanelL4.addBox(0.0F, 0.0F, -0.5F, 1, 3, 5);
      this.SidepanelL4.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.SidepanelL4.setTextureSize(128, 64);
      this.SidepanelL4.mirror = true;
      this.setRotation(this.SidepanelL4, 0.0F, 0.0F, (-(float)Math.PI / 6F));
      this.LegpanelC1 = new ModelRenderer(this, 16, 45);
      this.LegpanelC1.addBox(-3.0F, 0.0F, -0.5F, 6, 8, 1);
      this.LegpanelC1.setRotationPoint(0.0F, 5.5F, -3.0F);
      this.LegpanelC1.setTextureSize(128, 64);
      this.LegpanelC1.mirror = true;
      this.setRotation(this.LegpanelC1, 0.0F, 0.0F, 0.0F);
      this.LegpanelC2 = new ModelRenderer(this, 16, 54);
      this.LegpanelC2.addBox(-3.0F, 0.0F, -0.5F, 6, 4, 1);
      this.LegpanelC2.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.LegpanelC2.setTextureSize(128, 64);
      this.LegpanelC2.mirror = true;
      this.setRotation(this.LegpanelC2, 0.0F, 0.0F, 0.0F);
      this.LegpanelC3 = new ModelRenderer(this, 32, 59);
      this.LegpanelC3.addBox(-3.0F, 0.0F, -0.5F, 6, 4, 1);
      this.LegpanelC3.setRotationPoint(0.0F, 4.0F, 0.0F);
      this.LegpanelC3.setTextureSize(128, 64);
      this.LegpanelC3.mirror = true;
      this.setRotation(this.LegpanelC3, 0.0F, 0.0F, 0.0F);
      this.Cloak1 = new ModelRenderer(this, 106, 0);
      this.Cloak1.addBox(0.0F, 0.0F, -0.5F, 10, 18, 1);
      this.Cloak1.setRotationPoint(-5.0F, -6.0F, 4.0F);
      this.Cloak1.setTextureSize(128, 64);
      this.Cloak1.mirror = true;
      this.setRotation(this.Cloak1, 0.0F, 0.0F, 0.0F);
      this.Cloak2 = new ModelRenderer(this, 106, 19);
      this.Cloak2.addBox(0.0F, 0.0F, -0.5F, 10, 4, 1);
      this.Cloak2.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.Cloak2.setTextureSize(128, 64);
      this.Cloak2.mirror = true;
      this.setRotation(this.Cloak2, 0.0F, 0.0F, 0.0F);
      this.Cloak3 = new ModelRenderer(this, 106, 24);
      this.Cloak3.addBox(0.0F, 0.0F, -0.5F, 10, 4, 1);
      this.Cloak3.setRotationPoint(0.0F, 4.0F, 0.0F);
      this.Cloak3.setTextureSize(128, 64);
      this.Cloak3.mirror = true;
      this.setRotation(this.Cloak3, 0.0F, 0.0F, 0.0F);
      this.LegpanelC1.addChild(this.LegpanelC2);
      this.LegpanelC2.addChild(this.LegpanelC3);
      this.SidepanelL2.addChild(this.SidepanelL3);
      this.SidepanelL3.addChild(this.SidepanelL4);
      this.SidepanelR2.addChild(this.SidepanelR3);
      this.SidepanelR3.addChild(this.SidepanelR4);
      this.Cloak1.addChild(this.Cloak2);
      this.Cloak2.addChild(this.Cloak3);
   }

   public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
      this.partialTicks = p_78086_4_;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.BeltR.render(f5);
      this.Mbelt.render(f5);
      this.MbeltL.render(f5);
      this.MbeltR.render(f5);
      this.BeltL.render(f5);
      this.Chestplate.render(f5);
      this.Hood1.render(f5);
      this.Backplate.render(f5);
      this.ShoulderplateTopR.render(f5);
      this.ShoulderplateR1.render(f5);
      this.ShoulderplateR2.render(f5);
      this.ShoulderplateR3.render(f5);
      this.ShoulderR.render(f5);
      this.ArmL1.render(f5);
      this.ArmR1.render(f5);
      this.ShoulderL.render(f5);
      this.ShoulderplateLtop.render(f5);
      this.ShoulderplateL1.render(f5);
      this.ShoulderplateL2.render(f5);
      this.ShoulderplateL3.render(f5);
      this.LegpanelR4.render(f5);
      this.LegpanelR5.render(f5);
      this.LegpanelR6.render(f5);
      this.BackpanelR1.render(f5);
      this.BackpanelR2.render(f5);
      this.BackpanelR3.render(f5);
      this.BackpanelL3.render(f5);
      this.LegpanelL4.render(f5);
      this.LegpanelL5.render(f5);
      this.LegpanelL6.render(f5);
      this.BackpanelL1.render(f5);
      this.BackpanelL2.render(f5);
      this.Cloak1.render(f5);
      this.SidepanelR1.render(f5);
      this.SidepanelL1.render(f5);
      this.SidepanelL2.render(f5);
      this.SidepanelR2.render(f5);
      this.LegpanelC1.render(f5);
      if (entity instanceof EntityEldritchWarden) {
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);
         GL11.glScaled(1.01, 1.01, 1.01);
         int j = (int)(195.0F + MathHelper.sin((float)entity.ticksExisted / 3.0F) * 15.0F + 15.0F);
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         this.HoodEye.render(f5);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
      }

   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      this.Hood1.rotateAngleY = par4 / (180F / (float)Math.PI);
      this.Hood1.rotateAngleX = par5 / (180F / (float)Math.PI);
      this.HoodEye.rotateAngleX = this.Hood1.rotateAngleX;
      this.HoodEye.rotateAngleY = this.Hood1.rotateAngleY;
      float alr = 0.0F;
      float all = 0.0F;
      if (entity instanceof EntityEldritchGuardian) {
         alr = ((EntityEldritchGuardian)entity).armLiftR;
         all = ((EntityEldritchGuardian)entity).armLiftL;
      }

      if (entity instanceof EntityEldritchWarden) {
         alr = ((EntityEldritchWarden)entity).armLiftR;
         all = ((EntityEldritchWarden)entity).armLiftL;
      }

      this.ArmL1.rotateAngleX = -1.0F - all + MathHelper.sin(((float)(entity.ticksExisted + 20) + this.partialTicks) / 10.0F) * 0.08F;
      this.ArmR1.rotateAngleX = -1.0F - alr + MathHelper.sin(((float)entity.ticksExisted + this.partialTicks) / 10.0F) * 0.08F;
      this.LegpanelC1.rotateAngleX = -0.15F + MathHelper.sin(((float)entity.ticksExisted + this.partialTicks) / 8.0F) * 0.12F;
      this.LegpanelC2.rotateAngleX = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks - 5.0F) / 8.0F) * 0.13F;
      this.LegpanelC3.rotateAngleX = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks - 10.0F) / 8.0F) * 0.14F;
      this.Cloak1.rotateAngleX = 0.2F + MathHelper.sin(((float)entity.ticksExisted + this.partialTicks) / 7.0F) * 0.08F;
      this.Cloak2.rotateAngleX = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks - 5.0F) / 7.0F) * 0.1F;
      this.Cloak3.rotateAngleX = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks - 10.0F) / 7.0F) * 0.12F;
      this.SidepanelL2.rotateAngleZ = -0.2F + MathHelper.sin(((float)(entity.ticksExisted + 10) + this.partialTicks) / 8.0F) * 0.12F;
      this.SidepanelL3.rotateAngleZ = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks + 5.0F) / 8.0F) * 0.13F;
      this.SidepanelL4.rotateAngleZ = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks) / 8.0F) * 0.14F;
      this.SidepanelR2.rotateAngleZ = 0.2F + MathHelper.sin(((float)(entity.ticksExisted - 5) + this.partialTicks) / 8.0F) * 0.12F;
      this.SidepanelR3.rotateAngleZ = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks - 10.0F) / 8.0F) * 0.13F;
      this.SidepanelR4.rotateAngleZ = MathHelper.sin(((float)entity.ticksExisted + this.partialTicks - 15.0F) / 8.0F) * 0.14F;
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
