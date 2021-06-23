package com.example.go4lunch;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.utils.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class UtilsTest {

    private ArrayList<Restaurant> mRestaurants = generatorOfRestaurant();
    private ArrayList<Worker> mWorkers = generatorOfWorkers();


    private static List<Restaurant> mRestaurantsGenerate = Arrays.asList(
            new Restaurant(null,"le Zinc", "2 boulevard poissonnière",
                    "000", false,null,320,0, 2.5),
            new Restaurant(null,"KFC", "13 place lille",
                    "011", true,null,120,3, 4.5),
            new Restaurant(null,"Mac Donald", "14 place marquette",
                    "0022", false,null,500,0, 1.5),
           new Restaurant(null,"Pizza", "place pizza",
                   "0045", false,null,1000,0, 3)
    );

    private static List<Worker> mWorkersGenerate = Arrays.asList(
            new Worker("Olivier", null, "000", "le Zinc"),
            new Worker("Cyril", null, "011", "KFC"),
            new Worker("Carole", null, "0022 ", "Mac Donald"),
            new Worker("Katy", null, "0045 ", "Pizza" )
    );

    private static ArrayList<Restaurant> generatorOfRestaurant() {
        return new ArrayList<>(mRestaurantsGenerate);
    }

    private static ArrayList<Worker> generatorOfWorkers() {
        return new ArrayList<>(mWorkersGenerate);
    }

    @Test
    public void getChoiceRestaurantWithSuccess(){
        //create list restaurant
        ArrayList<Restaurant> restaurantArrayList = Utils.getChoiceRestaurants(mRestaurants, mWorkers);
        //assert first restaurant is choice
        assertTrue(restaurantArrayList.get(0).isChoice());
        //assert first restaurant has a worker
        assertEquals(1, restaurantArrayList.get(0).getWorkers());
        //assert third restaurant haven't worker
        assertEquals(0, restaurantArrayList.get(2).getWorkers());
        //assert second restaurant has 2 workers
        assertEquals(1, restaurantArrayList.get(1).getWorkers());
    }

    @Test
    public void triProximity_Success() {
        Utils.sortProximity(mRestaurants);
        assertEquals(mRestaurants.get(0).getNameRestaurant(), mRestaurants.get(0).getNameRestaurant());
        assertEquals(mRestaurants.get(1).getNameRestaurant(), mRestaurants.get(1).getNameRestaurant());
        assertEquals(mRestaurants.get(2).getNameRestaurant(), mRestaurants.get(2).getNameRestaurant());
        assertEquals(mRestaurants.get(3).getNameRestaurant(), mRestaurants.get(3).getNameRestaurant());
    }

    @Test
    public void triName_Success() {
        Utils.sortName(mRestaurants);
        assertEquals(mRestaurants.get(0).getNameRestaurant(), mRestaurants.get(0).getNameRestaurant());
        assertEquals(mRestaurants.get(1).getNameRestaurant(), mRestaurants.get(1).getNameRestaurant());
        assertEquals(mRestaurants.get(2).getNameRestaurant(), mRestaurants.get(2).getNameRestaurant());
        assertEquals(mRestaurants.get(3).getNameRestaurant(), mRestaurants.get(3).getNameRestaurant());
    }

    @Test
    public void triRatingReverse_Success() {
        Utils.sortRatingReverse(mRestaurants);
        assertEquals(mRestaurants.get(0).getNameRestaurant(), mRestaurants.get(0).getNameRestaurant());
        assertEquals(mRestaurants.get(1).getNameRestaurant(), mRestaurants.get(1).getNameRestaurant());
        assertEquals(mRestaurants.get(2).getNameRestaurant(), mRestaurants.get(2).getNameRestaurant());
        assertEquals(mRestaurants.get(3).getNameRestaurant(), mRestaurants.get(3).getNameRestaurant());
    }


}
