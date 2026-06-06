/*******************************************************************************
 * Copyright 2018 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.dakror.quarry.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.dakror.common.libgdx.PlatformInterface;
import de.dakror.common.libgdx.ui.Scene;
import de.dakror.quarry.Const;
import de.dakror.quarry.Quarry;
import net.spookygames.gdx.sfx.SfxMusic;
import net.spookygames.gdx.sfx.SfxMusicLoader;
import net.spookygames.gdx.sfx.SfxSound;
import net.spookygames.gdx.sfx.SfxSoundLoader;

/**
 * @author Maximilian Stark | Dakror
 */
public class LoadingScreen extends Scene {
    long l;

    Label label;

    TextureRegion progress;
    TextureRegion bg;

    float prog;
    float visualProg;
    float oldVisualProg;
    float interp;

    boolean finishingUp = false;

    @Override
    public void init() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        AssetManager assets = new AssetManager(resolver);
        Quarry.Q.assets = assets;
        assets.setLoader(SfxMusic.class, new SfxMusicLoader(resolver));
        assets.setLoader(SfxSound.class, new SfxSoundLoader(resolver));

        assets.load("tex.atlas", TextureAtlas.class);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
        fontMap.put("small-font", createFont(generator, 24));
        fontMap.put("default-font", createFont(generator, 32));
        fontMap.put("big-font", createFont(generator, 48));
        SkinParameter param = new SkinParameter("tex.atlas", fontMap);

        assets.load("skin.json", Skin.class, param);

        assets.finishLoading();

        Quarry.Q.atlas = assets.get("tex.atlas");


        Quarry.Q.skin = assets.get("skin.json");
        Quarry.Q.font = Quarry.Q.skin.getFont("default-font");

        Quarry.Q.font.getData().markupEnabled = true;
        Quarry.Q.font.setFixedWidthGlyphs("0123456789-+");
        Quarry.Q.skin.getFont("small-font").setFixedWidthGlyphs("0123456789");

        progress = Quarry.Q.atlas.findRegion("structure_conveyor_we");
        bg = Quarry.Q.atlas.findRegion("button");
        Quarry.Q.mouseTex = Quarry.Q.atlas.findRegion("mouse");

        ///////////////////////////

        stage = new Stage(new FitViewport(Const.UI_W, Const.UI_H));
        stage.setActionsRequestRendering(false);
        label = new Label(Quarry.Q.i18n.get("loading.sounds"), Quarry.Q.skin);
        label.setAlignment(Align.center);
        Table t = new Table();
        t.setBackground(Quarry.Q.skin.getTiledDrawable("tile_stone"));
        t.add(label).grow();
        t.setSize(Const.UI_W, Const.UI_H);
        stage.addActor(t);

        ///////////////////////////

        assets.load("sfx/airpurifier" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/anchorportal" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/arcwelder" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/assembler" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/ballmill" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/bender" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/boiler" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/booster" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/carpenter" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/centrifuge" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/charcoalmound" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/column" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/compactor" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/condenser" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/crucible" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/devicefabricator" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/digitalstorage" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/drawer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/excavator" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/furnace" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/injection" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/kiln" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/mason" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/mine" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/mixer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/node" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/oilwell" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/polarizer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/polymerizer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/pump1" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/refinery" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/rockcrusher" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/rollingmachine" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/sawmill" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/science" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/shaftdrill" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/stacker" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/turbine" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/waterwheel" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/woodcutter" + Const.SFX_FORMAT, SfxSound.class);

        assets.load("sfx/click3" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/build" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/destroy" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/cable" + Const.SFX_FORMAT, Sound.class);

        assets.load("sfx/ambience_empty" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/ambience_base" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/ambience_heavy" + Const.SFX_FORMAT, Sound.class);

        assets.load("music/Fading_into_the_Dream" + Const.MUSIC_FORMAT, Music.class);
        assets.load("music/Impact Prelude" + Const.MUSIC_FORMAT, Music.class);

        l = System.currentTimeMillis();
    }

    protected BitmapFont createFont(FreeTypeFontGenerator gen, float dp) {
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.magFilter = TextureFilter.Linear;
        param.minFilter = TextureFilter.Linear;
        param.size = (int) (dp * (float) Quarry.Q.pi.message(Const.MSG_DPI, null));

        // 基础拉丁字符
        StringBuilder sb = new StringBuilder();
        sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-+!@#$%^&*()_=[]{};:'/\",.<>?\\|`~");
//        // 添加基本汉字范围 (U+4E00 到 U+9FA5)
//        for (char c = '\u4e00'; c <= '\u9fa5'; c++) {
//            sb.append(c);
//        }
        sb.append("一上下不与且世业东丝两个中串为主么之乏买了事于互五井些交产享亮人什仅从仓仔他以们件价任伐优会传但位低体何余作你使供依侧便保倒值" +
                "停储像允元充先光入全六共其具养内再写冲况冶冷净准凝几出击分切创初删利到制剂前剩剪割力功加动助势勤匀包化匠区千升单卡即却卸厂压原去叉" +
                "及双反发取受变叠口另只可台右叶合同名后向吗否含启呀告周命和品哎商喜器回围固图圆土在地场均块坚坩型垛埃埋城埚域培基堆塑塔填增壮声壳处" +
                "备复外多够大天太失头夹奇套好如始子字存学它完定宝实室容密察寸对导封将小少尘就尺尽层屏属岩峰巅工左巨己已布希带常幕平并库应底店度建开" +
                "弄式引张弧弯强当录形彼往征待很得循心必志快态思性总恢恭息您情意感慎慢戏成我或户所手才打托扩扭技把折护报抽拆拉拒拖择拽持指按挖换据掘" +
                "接控推描提摆摘撤支收改放效教数整文斗料断新方施旋无时是显普晶暂曲更最有望木未本术机杂杆材束条来板极构果架某查柱标栋树样核根格档桥桶" +
                "梯械棒植模次欢止正此步段每比气水永求汇池汽沙没沸油沿泄法泡泥注泵洒洗活流浆浸消涡润液深混温游源滑滚满滤漂漏演激灌火炉炭炸点炼烧热焊" +
                "焦然煤熔燃爆片版物特状独率玩环现玻珠球理璃生用由电界的盐盖盘目直相看真着知矩石矿码砍砖础硅硫硬确碎碳磁磨磺示离种科秒称移稀程稳空窑" +
                "立竖端第等筑筒筛简算管箱类粉粗粘精纤级纯纸线组细终经结绕绘绝继续维绿缆缓编缩缺罐网置考者而耐耗联聚胜能脉脚自至致航良色节芯英药获菜" +
                "萃蒸蓝藏虑融行表被装裹西要覆见观规视角解言计让许论设访识试该语误请诸读调谢谨败质购贴资起超越足车轨转轮软轴载较辅辊输边达过迎运近返" +
                "还这进远连述退送适选通速造道邻部都配采里重野量金钛钢钮钻铁铅铜银铸链销锁错锚锡锭键锯长门问间阀防阳阵附降限除陶随隧集需青非面音顶项" +
                "须预题颜馈馏首验骤高黄黑，。（）“”！？");
        param.characters = sb.toString();

        BitmapFont font = gen.generateFont(param);
        font.getData().markupEnabled = true;
        return font;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        float p = Quarry.Q.assets.getProgress();

        if (p != prog) {
            oldVisualProg = visualProg;
            interp = 0;
            prog = p;
        }

        if (visualProg != prog) {
            if (interp >= 1) {
                visualProg = prog;
                interp = 0;
            } else {
                visualProg = oldVisualProg + (prog - oldVisualProg) * interp;
                interp += deltaTime / 0.1f;
            }
        }

        if (Quarry.Q.assets.getProgress() > 0.9f) {
            label.setText(Quarry.Q.i18n.get("loading.buildings"));
        }

        try {
            if (!finishingUp && Quarry.Q.assets.update() && visualProg == 1 && !fadeOut) {
                System.out.println("Asset loading took " + (System.currentTimeMillis() - l) + " ms");
                finishingUp = true;
                Quarry.Q.loadingFinished();
            }
        } catch (Exception e) {
            Quarry.Q.pi.message(PlatformInterface.MSG_EXCEPTION, e);
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(127 / 256f, 127 / 256f, 127 / 256f, 1);
        super.draw();

        stage.getBatch().begin();

        stage.getBatch().setColor(0, 0, 0, 0.5f);

        int x = (int) ((Const.UI_W - 600) / 2);
        stage.getBatch().draw(bg, x, Const.UI_H / 4, 600, 40);
        stage.getBatch().setColor(1, 1, 1, 1);

        float p = 600 * visualProg;
        for (int i = 0; i < (int) (p) / 64; i++)
            stage.getBatch().draw(progress, x + i * 64, Const.UI_H / 4 - 12);

        float len = (int) (p / 64) * 64;
        stage.getBatch().draw(progress.getTexture(), x + len, Const.UI_H / 4 - 12, p - len, 64, progress.getU(), progress.getV2(), (progress.getU2() - progress.getU()) * (p - len) / 64 + progress.getU(), progress.getV());
        stage.getBatch().end();
    }
}
