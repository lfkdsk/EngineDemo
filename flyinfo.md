## 使用JustWeEngine开发微信打飞机：

作者博客: [博客园](http://www.cnblogs.com/lfk-dsk/)  
引擎地址:[JustWeEngine](https://github.com/lfkdsk/JustWeEngine)  
示例代码:[EngineDemo](https://github.com/lfkdsk/EngineDemo)

### JustWeEngine?

JustWeEngine是托管在Github 的一个开源的Android原生开发框架，可以让Android的开发人员
非常便捷，无需切换语言和编译器的制作Android原生游戏。

## 使用方法  
* 引入Engine作为Library进行使用。
* 引入"/jar"文件夹下的jar包。  
* 使用Gradle构建:  
  * Step 1. Add the JitPack repository to your build file  
  Add it in your root build.gradle at the end of repositories:  
  ``` java  
  
    	allprojects {
			repositories {
				...
				maven { url "https://jitpack.io" }
			}
		}
   	
  ```
  
  * Step 2. Add the dependency  
  
  ``` java
  
      dependencies {
	        compile 'com.github.lfkdsk:JustWeEngine:v1.01'
	  }
		
  ```
* 使用Maven构建:  
  * Step 1. Add the JitPack repository to your build file  
  
  ``` xml
  
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
  ```
  
  * Step 2. Add the dependency  
  
  ``` xml
  	
    <dependency>
	    <groupId>com.github.lfkdsk</groupId>
	    <artifactId>JustWeEngine</artifactId>
	    <version>v1.01</version>
	</dependency>
	
  ```

## 1.Activity继承基类：
   由于框架全部使用SurfaceView进行绘制，不使用诸如Button、Layout等原生控件，所以应该首先新建类继承引擎核心类Engine，负责游戏的流程，注释中已有明确的标明功能。  
   
``` java

	public class Game extends Engine {
	// 一般在构造函数完成变量的初始化
    public Game() {
    	// 控制debug模式是否开始，debug模式打印日志、帧数、pause次数等信息
        super(true);
        
    }
	
	// 载入一些UI参数和设置屏幕放向，默认背景色，设定屏幕的扫描方式等
    @Override
    public void init() {
    	// 初始化UI默认参数，必须调用该方法，其中有一些用于多机型适配的参数需要初始化
        UIdefaultData.init(this);
    }

	// 通常用于精灵，背景，图片等物体的设置和载入
    @Override
    public void load() {

    }

	// draw 和 update 在线程中进行不断的循环刷新
	// draw 负责绘制 update 负责更新数据和对象信息
    @Override
    public void draw() {

    }

    @Override
    public void update() {

    }
	
	// 将touch 事件传回 功能和所设定的屏幕扫描方式有关
    @Override
    public void touch(MotionEvent event) {

    }
    
	// 将碰撞事件传回 传回精灵和物品的基类 
	// 用于处理碰撞事件 默认使用矩形碰撞
    @Override
    public void collision(BaseSub baseSub) {

    }
    }

```   
  
## 2.添加图片、文字、对象：
### 2.1 添加文字：

        printer = new GameTextPrinter();
        printer.setTextColor(Color.BLACK);
        printer.setTextSize(24);
        printer.setLineSpaceing(28);

初始化文字绘制printer，并设置颜色尺寸和图片。

    @Override
    public void draw() {
        canvas = super.getCanvas();
        printer.setCanvas(canvas);
        printer.drawText("Engine demo", 10, 20);
    }
在draw中即可绘制。
### 2.2 使用图片：
首先添加一张大的图片，需要的敌军战机，和可控的飞机都在这张图上。

        GameTexture texture1 = new GameTexture(this);
        texture1.loadFromAsset("pic/shoot.png");

tupian

并且初始化敌人的图片和子弹的图片：
		
		shoot = new GameTexture(this);
        shoot.loadFromAsset("pic/flare.png");

        enemyPic = new GameTexture(this);
        enemyPic.loadFromAsset("pic/enemy.png");

### 2.3 对象：
我们要新建一个BaseSprite对象并设置一些基础属性。

    	BaseSprite ship;
		// 新建对象，每张帧图片宽100高124
        ship = new BaseSprite(this, 100, 124, FrameType.COMMON);
        // 从刚才的那张图片取出帧
        ship.setTexture(texture1);
        // 添加两帧 参数是坐标
        ship.addRectFrame(0, 100, 100, 124);
        ship.addRectFrame(167, 361, 100, 124);
        // 添加动画 在两帧之间不断切换
        ship.addAnimation(new FrameAnimation(0, 1, 1));
        // 设定位置
        ship.setPosition(UIdefaultData.centerInHorizontalX - ship.getWidthWithScale() / 2,
                UIdefaultData.screenHeight + ship.getHeightWidthScale());
        // 设定拉伸后的大小
        ship.setDipScale(96, 96);
        // 添加固定动画 即小飞机出场
        ship.addfixedAnimation("start",
                new MoveAnimation(UIdefaultData.centerInHorizontalX - ship.getWidthWithScale() / 2,
                        UIdefaultData.screenHeight - 2 * ship.getHeightWidthScale(), new Float2(10, 10)));
        // 设定名称和类别 对象分组
        ship.setName("SHIP");
        ship.setIdentifier(SHIP);
        // 添加到精灵组中
        addToSpriteGroup(ship);

### 2.4添加背景：

        GameTexture tex = new GameTexture(this);
        if (!tex.loadFromAsset("pic/background.png")) {
            fatalError("Error loading space");
        }
        backGround2X = Bitmap.createBitmap(
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backGround2X);
        Rect dst = new Rect(0, 0, UIdefaultData.screenWidth - 1,
                UIdefaultData.screenHeight);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
        dst = new Rect(0, UIdefaultData.screenHeight,
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight * 2);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
                bg_rect = new Rect(0, 0, UIdefaultData.screenWidth, 		UIdefaultData.screenHeight);
        bg_scroll = new Point(0, 0);

这里获取了一张背景图片，并且创建了一张Bitmap包含两份的背景图片，这是为了让背景滚动起来。

### 2.5生成子弹、敌人：

	public void fireBullet() {
        if (!shoottimer.stopWatch(300)) return;
        BaseSprite bullet;
        if (getTypeSizeFromRecycleGroup(BULLET) > 0) {
            bullet = (BaseSprite) recycleSubFromGroup(BULLET);
            bullet.clearAllAnimation();
            removeFromRecycleGroup(bullet);
        } else {
            bullet = new BaseSprite(this);
            bullet.setTexture(shoot);
            bullet.setDipScale(8, 18);
            bullet.setIdentifier(BULLET);
        }
        double angle = 270.0;
        float speed = 20.0f;
        int lifetime = 2500;
        bullet.addAnimation(new V elocityAnimation(angle, speed,
                lifetime));
        bullet.setPosition(ship.s_position.x +
                        ship.getWidthWithScale() / 2
                        - bullet.getWidthWithScale() / 2,
                ship.s_position.y - 24);
        bullet.setAlive(true);
        addToSpriteGroup(bullet);
    }

    private void addEnemy() {
        BaseSprite enemy;
        if (getTypeSizeFromRecycleGroup(ENEMY) > 0) {
            enemy = (BaseSprite) recycleSubFromGroup(ENEMY);
            enemy.clearAllAnimation();
            removeFromRecycleGroup(enemy);
        } else {
            enemy = new BaseSprite(this);
            enemy.setTexture(enemyPic);
            enemy.setIdentifier(ENEMY);
            enemy.setDipScale(49, 36);
        }
        double angle = 90.0;
        float speed = 5.0f;
        int lifetime = 5000;
        enemy.addAnimation(new VelocityAnimation(angle, speed,
                lifetime));
        enemy.setPosition(random.nextInt(UIdefaultData.screenWidth),
                -enemy.getWidthWithScale());
        enemy.setAlive(true);
        addToSpriteGroup(enemy);
        enemyNum++;
    }


### 2.6进行绘制：
更新draw 和 update的方法进行绘制：

	@Override
    public void draw() {
        canvas = super.getCanvas();
        canvas.drawBitmap(backGround2X, bg_rect, bg_rect, paint);
        printer.setCanvas(canvas);
        printer.drawText("Engine demo", 10, 20);
    }

    @Override
    public void update() {
    	// 
        if (timer.stopWatch(20)) {
            scrollBackground();
        }
        // 进行入场动画
        if (ship.getFixedAnimation("start").animating) {
            ship.fixedAnimation("start");
        } else {
        // 射击和新建敌人
            fireBullet();
	//      int size = getTypeSizeFromRecycleGroup(ENEMY);
	//      if (size > 0)
	//      enemyNum -= size;
            if (enemyTimer.stopWatch(200)) {
                addEnemy();
            }
        }
    }
### 2.7碰撞监测和touch事件：

    @Override
    public void touch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (ship.s_position.x + offsetX > 0
                            && ship.s_position.x + offsetX +
                            ship.getHeightWidthScale() < UIdefaultData.screenWidth) {
                        ship.s_position.x += offsetX;
                        resetEvent(event);
                    }
                } else {
                    if (ship.s_position.y + offsetY > 0
                            && ship.s_position.y + offsetY +
                            ship.getHeightWidthScale() < UIdefaultData.screenHeight) {
                        ship.s_position.y += offsetY;
                        resetEvent(event);
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
        }
    }

    @Override
    public void collision(BaseSub baseSub) {
        BaseSprite other = (BaseSprite) baseSub.getOffender();
        if (baseSub.getIdentifier() == BULLET &&
                other.getIdentifier() == ENEMY) {
            other.setAlive(false);
            removeFromSpriteGroup(other);
            addToRecycleGroup(other);
            enemyNum--;
        }
    }
    
### 2.8滚动背景:

    private void resetEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }

    public void scrollBackground() {
        bg_scroll.y += 10.0f;
        bg_rect.top = bg_scroll.y;
        bg_rect.bottom = bg_rect.top + UIdefaultData.screenHeight - 1;
        if (bg_scroll.y + bg_rect.height() > backGround2X.getHeight()) {
            bg_scroll.y = bg_scroll.y - bg_rect.height();
        }
    }
