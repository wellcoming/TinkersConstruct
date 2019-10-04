package slimeknights.tconstruct.gadgets.entity;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IStringSerializable;
import slimeknights.tconstruct.gadgets.TinkerGadgets;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public enum FrameType implements IStringSerializable {
  JEWEL(0),
  ALUMINUM_BRASS(1),
  COBALT(2),
  ARDITE(3),
  MANYULLYN(4),
  GOLD(5),
  CLEAR(6);

  private static final FrameType[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(FrameType::getId)).toArray((id) -> {
    return new FrameType[id];
  });
  private final int id;

  private FrameType(int idIn) {
    this.id = idIn;
  }

  public int getId() {
    return this.id;
  }

  public static FrameType byId(int id) {
    if (id < 0 || id >= VALUES.length) {
      id = 0;
    }

    return VALUES[id];
  }

  @Override
  public String getName() {
    return this.toString().toLowerCase(Locale.US);
  }

  public static Item getFrameFromType(FrameType type) {
    switch (type) {
      case JEWEL:
        return TinkerGadgets.jewel_item_frame;
      case ALUMINUM_BRASS:
        return TinkerGadgets.aluminum_brass_item_frame;
      case COBALT:
        return TinkerGadgets.cobalt_item_frame;
      case ARDITE:
        return TinkerGadgets.ardite_item_frame;
      case MANYULLYN:
        return TinkerGadgets.manyullyn_item_frame;
      case GOLD:
        return TinkerGadgets.gold_item_frame;
      case CLEAR:
        return TinkerGadgets.clear_item_frame;
      default:
        return Items.ITEM_FRAME;
    }
  }
}