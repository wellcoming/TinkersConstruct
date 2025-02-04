package slimeknights.tconstruct.library.modifiers.hook.interaction;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
import java.util.function.Function;

/**
 * Hooks for standard interaction logic though entities. See {@link GeneralInteractionModifierHook} for general interaction and {@link BlockInteractionModifierHook} for blocks.
 */
public interface EntityInteractionModifierHook {
  /** Default instance that performs no action */
  EntityInteractionModifierHook EMPTY = new EntityInteractionModifierHook() {};
  /** Merger that returns when the first hook succeeds */
  Function<Collection<EntityInteractionModifierHook>, EntityInteractionModifierHook> FIRST_MERGER = FirstMerger::new;

  /**
	 * Called when interacting with an entity before standard entity interaction.
   * In general, its better to use {@link #afterEntityUse(IToolStackView, ModifierEntry, Player, LivingEntity, InteractionHand, InteractionSource)} for behavior more consistent with vanilla.
   * @param tool       Tool performing interaction
   * @param modifier   Modifier instance
   * @param player     Interacting player
   * @param target     Target of interaction
   * @param hand       Hand used for interaction
   * @param source     Source of the interaction
   * @return  Return PASS or FAIL to allow vanilla handling, any other to stop vanilla and later modifiers from running.
	 */
  default InteractionResult beforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, Entity target, InteractionHand hand, InteractionSource source) {
    return InteractionResult.PASS;
  }

  /**
   * Called when interacting with an entity after standard entity interaction.
   * @param tool       Tool performing interaction
   * @param modifier   Modifier instance
   * @param player     Interacting player
   * @param target     Target of interaction
   * @param hand       Hand used for interaction
   * @param source     Source of the interaction
   * @return  Return PASS or FAIL to allow vanilla handling, any other to stop vanilla and later modifiers from running.
   */
  default InteractionResult afterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, LivingEntity target, InteractionHand hand, InteractionSource source) {
    return InteractionResult.PASS;
  }

  /** Logic to merge multiple interaction hooks into one */
  record FirstMerger(Collection<EntityInteractionModifierHook> modules) implements EntityInteractionModifierHook {
    @Override
    public InteractionResult beforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, Entity target, InteractionHand hand, InteractionSource source) {
      InteractionResult result = InteractionResult.PASS;
      for (EntityInteractionModifierHook module : modules) {
        result = module.beforeEntityUse(tool, modifier, player, target, hand, source);
        if (result.consumesAction()) {
          return result;
        }
      }
      return result;
    }

    @Override
    public InteractionResult afterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, LivingEntity target, InteractionHand hand, InteractionSource source) {
      InteractionResult result = InteractionResult.PASS;
      for (EntityInteractionModifierHook module : modules) {
        result = module.afterEntityUse(tool, modifier, player, target, hand, source);
        if (result.consumesAction()) {
          return result;
        }
      }
      return result;
    }
  }

  /** Fallback logic calling old hooks, remove in 1.19 */
  @SuppressWarnings("DeprecatedIsStillUsed")
  @Deprecated
  EntityInteractionModifierHook FALLBACK = new EntityInteractionModifierHook() {
    @Override
    public InteractionResult beforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, Entity target, InteractionHand hand, InteractionSource source) {
      if (source != InteractionSource.LEFT_CLICK) {
        return modifier.getModifier().beforeEntityUse(tool, modifier.getLevel(), player, target, hand, source.getSlot(hand));
      }
      return InteractionResult.PASS;
    }

    @Override
    public InteractionResult afterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, LivingEntity target, InteractionHand hand, InteractionSource source) {
      if (source != InteractionSource.LEFT_CLICK) {
        return modifier.getModifier().afterEntityUse(tool, modifier.getLevel(), player, target, hand, source.getSlot(hand));
      }
      return InteractionResult.PASS;
    }
  };
}
