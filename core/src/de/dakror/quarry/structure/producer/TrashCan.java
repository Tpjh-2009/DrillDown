package de.dakror.quarry.structure.producer; // 或新建包

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.dakror.common.BiCallback;
import de.dakror.common.libgdx.io.NBT.Builder;
import de.dakror.common.libgdx.io.NBT.CompoundTag;
import de.dakror.common.libgdx.io.NBT.NBTException;
import de.dakror.quarry.Const;
import de.dakror.quarry.Quarry;
import de.dakror.quarry.game.Item.ItemType;
import de.dakror.quarry.game.Item.Items;
import de.dakror.quarry.game.Science.ScienceType;
import de.dakror.quarry.scenes.Game;
import de.dakror.quarry.structure.Booster;
import de.dakror.quarry.structure.base.Direction;
import de.dakror.quarry.structure.base.Dock;
import de.dakror.quarry.structure.base.Dock.DockFilter;
import de.dakror.quarry.structure.base.Dock.DockType;
import de.dakror.quarry.structure.base.Schema;
import de.dakror.quarry.structure.base.Schema.Flags;
import de.dakror.quarry.structure.base.Structure;
import de.dakror.quarry.structure.base.StructureType;
import de.dakror.quarry.util.Sfx;

import java.util.Arrays;

/**
 * 垃圾桶 - 销毁任何输入物品
 * 基于 Booster 的简单输入处理模式
 */
public class TrashCan extends Structure<Schema> {
    public static final Schema classSchema = new Schema(0, StructureType.TrashCan, true, 1, 1,
            "mine",
            new Items(ItemType.Stone, 5),
            new Sfx("mine" + Const.SFX_FORMAT),
            new Dock(0, 0, Direction.North, DockType.ItemIn),
            new Dock(0, 0, Direction.South, DockType.ItemIn),
            new Dock(0, 0, Direction.East, DockType.ItemIn),
            new Dock(0, 0, Direction.West, DockType.ItemIn));

//    private long totalDestroyed = 0;       // 累计销毁物品数量

    public TrashCan(int x, int y) {
        super(x, y, classSchema);
    }

    @Override
    public boolean canAccept(ItemType item, int x, int y, Direction dir) {
        // 检查是否有对应的输入 Dock，且通过过滤器
        for (Dock d : getDocks()) {
            if (d.type == DockType.ItemIn && isNextToDock(x, y, dir, d)) {
                return d.filter == null || d.filter.accepts(item);
            }
        }
        return false;
    }

    @Override
    public boolean acceptItem(ItemType item, Structure<?> source, Direction dir) {
        // 检查该方向的 Dock 是否存在且允许该物品
//        Dock dock = Arrays.stream(getDocks()).findFirst().get();
//        if (dock == null || dock.type != DockType.ItemIn) return false;
//        if (dock.filter != null && !dock.filter.accepts(item)) return false;

        // 直接销毁物品，累加计数
//        totalDestroyed++;

        // 可选：触发附近结构更新（如链式反应）
        return true;
    }

    @Override
    public int getReceiverPriority() {
        // 高优先级，确保物品优先被销毁
        return 200;
    }

//    @Override
//    public void onClick(Table content) {
//        super.onClick(content);
//        // 显示销毁统计
//        Label info = new Label(Quarry.Q.i18n.get("trashcan.destroyed") + ": " + totalDestroyed, Quarry.Q.skin);
//        content.add(info).left().pad(10);
//        // 可选：显示处理速度（itemsDestroyedThisTick / 帧时间）
//    }

//    @Override
//    protected void saveData(Builder b) {
//        super.saveData(b);
//        b.Long("totalDestroyed", totalDestroyed);
//    }

//    @Override
//    protected void loadData(CompoundTag tag) throws NBTException {
//        super.loadData(tag);
//        totalDestroyed = tag.Long("totalDestroyed", 0);
//    }

    // 可选：在每帧重置计数器（用于显示速率）
    // 可覆写 update 来统计每秒销毁数
}