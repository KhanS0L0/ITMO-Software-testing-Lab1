package third;

import org.junit.jupiter.api.*;
import third.Decorations.Decoration;
import third.Decorations.Stone;
import third.Decorations.Table;
import third.Decorations.Umbrella;
import third.Entities.CrabHumanoid;
import third.Entities.Human;
import third.Entities.Humanoid;
import third.Locations.Beach;
import third.Locations.Sea;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("(=ʘᆽʘ=)∫")
public class ModelTest {

    private Beach beach;
    private Sea sea;
    private List<Human> humans;
    private List<Humanoid> humanoids;
    private List<CrabHumanoid> crabHumanoids;
    private List<Decoration> decorations;

    @BeforeAll
    void beforeAll() {
        humans = new ArrayList<>();
        humanoids = new ArrayList<>();
        crabHumanoids = new ArrayList<>();
        decorations = new ArrayList<>();

        Human human = new Human("Adam", "Adam", 31);
        humans.add(human);

        Humanoid humanoid = new Humanoid("first", "humanoid", 344);
        humanoids.add(humanoid);

        CrabHumanoid crabHumanoid = new CrabHumanoid("Eugene", "Harold Krabs", 52);
        crabHumanoids.add(crabHumanoid);

        Table table = new Table("Пляжный столик", "Просто столик", "Серебро");
        Stone stone = new Stone("Галька", "Мелкая желтая и зеленая галька", "Просто камень");
        Umbrella umbrella = new Umbrella("Пляжный зонтик", "Лиловый солнечный зонт с оборками и серебряными кистями", "Наверное пластик");
        decorations.add(table);
        decorations.add(stone);
        decorations.add(umbrella);
    }

    @BeforeEach
    void beforeEach(){
        beach = new Beach();
        sea = new Sea();
        beach.setLocationName("Bikini Bottom");
        sea.setLocationName("Black Sea");
    }

    @Test
    void testInitFalseWar(){
        assertFalse(beach.isWar());
        assertFalse(sea.isWar());
    }

    @Test
    void testNotCorrectLocation(){
        beach.setFirstSide(crabHumanoids);
        assertNull(beach.getFirstSide());
        beach.setSecondSide(crabHumanoids);
        assertNull(beach.getSecondSide());
        sea.setFirstSide(humans);
        assertNull(sea.getFirstSide());
        sea.setSecondSide(humans);
        assertNull(sea.getSecondSide());
    }

    @Test
    void testDecorations(){
        beach.setDecorations(decorations);
        assertNotNull(beach.getDecorations());

        Table table = (Table) decorations.get(0);
        assertEquals(table.getName(), "Пляжный столик");
        assertEquals(table.getMaterial(), "Серебро");

        Umbrella umbrella = (Umbrella) decorations.get(2);
        assertEquals(umbrella.getName(), "Пляжный зонтик");
        assertEquals(umbrella.getMaterial(), "Наверное пластик");

        Stone stone = (Stone) decorations.get(1);
        assertNotEquals(stone.getMaterial(), "Алмаз");
        assertNotEquals(stone.getName(), "Валун");
    }

    @Test
    void testBeachWar(){
        beach.setFirstSide(humans);
        beach.setSecondSide(humanoids);

        assertNotNull(beach.getFirstSide());
        assertNotNull(beach.getSecondSide());
        assertFalse(beach.isWar());

        humans.get(0).startWar(beach);
        assertTrue(beach.isWar());
        humans.get(0).stopWar(beach);
        assertFalse(beach.isWar());

        beach.setSecondSide(humans);
        humans.get(0).startWar(beach);
        assertFalse(beach.isWar());
    }

    @Test
    void testSeaWar(){
        sea.setFirstSide(crabHumanoids);
        sea.setSecondSide(humanoids);

        assertNotNull(sea.getSecondSide());
        assertNotNull(sea.getFirstSide());
        assertFalse(sea.isWar());

        crabHumanoids.get(0).startWar(sea);
        assertTrue(sea.isWar());
        crabHumanoids.get(0).stopWar(sea);
        assertFalse(sea.isWar());

        sea.setSecondSide(crabHumanoids);
        crabHumanoids.get(0).startWar(sea);
        assertFalse(sea.isWar());
    }

    @Test
    void humanoidsWar(){
        sea.setFirstSide(humanoids);
        sea.setSecondSide(humanoids);

        assertNotNull(sea.getSecondSide());
        assertNotNull(sea.getFirstSide());
        assertFalse(sea.isWar());

        humanoids.get(0).startWar(sea);
        assertFalse(sea.isWar());

        sea.setSecondSide(crabHumanoids);
        humanoids.get(0).startWar(sea);
        assertTrue(sea.isWar());
        humanoids.get(0).stopWar(sea);
        assertFalse(sea.isWar());

        beach.setFirstSide(humanoids);
        beach.setSecondSide(humanoids);

        assertNotNull(beach.getSecondSide());
        assertNotNull(beach.getFirstSide());
        assertFalse(beach.isWar());

        humanoids.get(0).startWar(beach);
        assertFalse(beach.isWar());

        beach.setSecondSide(humans);
        humanoids.get(0).startWar(beach);
        assertTrue(beach.isWar());
        humanoids.get(0).stopWar(beach);
        assertFalse(beach.isWar());
    }


}
