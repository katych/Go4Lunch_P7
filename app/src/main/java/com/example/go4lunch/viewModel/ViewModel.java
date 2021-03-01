package com.example.go4lunch.viewModel;
import com.example.go4lunch.model.Position;

public class ViewModel {


    /**
     * generate user position
     *
     * @param lat latitude
     * @param lng longitude
     * @return Position
     */
    public static Position generateUserPosition(double lat, double lng) {
        return new Position("My position",
                "",
                lat,
                lng
        );
    }
}
