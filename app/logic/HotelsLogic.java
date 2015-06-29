package logic;

import models.Client;
import models.Hotel;
import play.db.jpa.JPA;

/**
 * Created by wukat on 23.06.15.
 */
public class HotelsLogic {
    public static boolean newHotel(Hotel hotel, String clientEmail) {
        if (ClientLogic.isBusinessClient(clientEmail)) {
            hotel.setClientPublisher(Client.getClientByEmail(clientEmail));
            JPA.em().persist(hotel);
            return true;
        }
        return false;
    }

    public static boolean editHotel(Integer hotelId, Hotel hotel, String clientEmail) {
        if (ClientLogic.isBusinessClient(clientEmail)) {
            Hotel hotelOld = Hotel.getById(hotelId);
            if (hotelOld != null && hotelOld.getClientPublisher().getEmail().equals(clientEmail)) {
                hotelOld.setName(hotel.getName());
                hotelOld.setStandard(hotel.getStandard());
                JPA.em().merge(hotel);
                return true;
            }
        }
        return false;
    }

    public static boolean isClientsHotel(Hotel hotel, String email) {
        return hotel.getClientPublisher().getEmail().equals(email);
    }
}
