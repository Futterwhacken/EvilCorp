import evilcorp.graphic.Engine;
import evilcorp.graphic.gameobjects.Button;
import evilcorp.graphic.gameobjects.Text;
import evilcorp.graphic.gameobjects.Visual;
import evilcorp.graphic.gfx.Image;


import evilcorp.logic.GameMaster;
import evilcorp.logic.area.region.Region;





public class Test {
    public static Engine engine;
    public static GameMaster gm;

    public static Region getRegion(String regionName) {
        for (Region r : gm.getWorld().getRegions()) {
            if (r.getName().equals(regionName)) {
                return r;
            }
        }

        return null;
    }

    public static void main(String[] args) {

        engine = new Engine("");
        gm = GameMaster.initGameMaster("data/config/logic/");

        Image mapImage = new Image("/resources/maps/map.png");
        Image mapAfricaImage = new Image("/resources/maps/map-africa.png");
        Image mapAsiaImage = new Image("/resources/maps/map-asia.png");
        Image mapEuropeImage = new Image("/resources/maps/map-europe.png");
        Image mapNorthAmericaImage = new Image("/resources/maps/map-northamerica.png");
        Image mapSouthAmericaImage = new Image("/resources/maps/map-southamerica.png");



        Visual map = new Visual(engine, mapImage, 135, 0);

        // add color parameter for label (or directly take text ?), add simplified constructors
        // link region logic config with region graphic config through the name (string) of the region
        Button africaButton = new Button(engine, 296,90, 77, 85,
                () -> {
                map.setVisual(mapAfricaImage);
                engine.setSelectedRegion(getRegion("Africa"));
                }, null, null);
        Button asiaButton = new Button(engine, 374, 36, 140, 144,
                () -> {
                map.setVisual(mapAsiaImage);
                engine.setSelectedRegion(getRegion("Asia"));
                }, null, null);
        Button europeButton = new Button(engine, 306, 22, 67, 67,
                () -> {
                map.setVisual(mapEuropeImage);
                engine.setSelectedRegion(getRegion("Europe"));
                }, null, null);
        Button northAmericaButton = new Button(engine, 136, 21, 153, 92,
                () -> {
                map.setVisual(mapNorthAmericaImage);
                engine.setSelectedRegion(getRegion("North America"));
                }, null, null);
        Button southAmericaButton = new Button(engine, 215, 115, 63, 87,
                () -> {
                map.setVisual(mapSouthAmericaImage);
                engine.setSelectedRegion(getRegion("South America"));
                }, null, null);

        Text selectedRegionText = new Text(engine, "Select a region", 10, 10, 0xffffffff);
        selectedRegionText.setAction(() -> {
            if (engine.getSelectedRegion() != null)
                selectedRegionText.setText(engine.getSelectedRegion().getName());
            else
                selectedRegionText.setText(selectedRegionText.getDefaultText());
            });


        engine.addGameObject(map);
        engine.addGameObject(africaButton);
        engine.addGameObject(asiaButton);
        engine.addGameObject(europeButton);
        engine.addGameObject(northAmericaButton);
        engine.addGameObject(southAmericaButton);
        engine.addGameObject(selectedRegionText);



        engine.start();
    }
}
