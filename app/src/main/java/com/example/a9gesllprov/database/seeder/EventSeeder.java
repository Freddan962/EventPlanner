package com.example.a9gesllprov.database.seeder;

import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;

import java.util.Calendar;

/**
 * Responsible for seeding events for the application.
 * Mainly used during development.
 */
public class EventSeeder implements EntitySeeder {

    // The entities to be created with the following attributes.
    // Name, startYear, startMonth, startDay, startHour, startMinute,
    // endYear, endMonth, endDay, endHour, endMinute, phoneNumber, picturePath,
    // description, youtubeUrl, homePage
    private String[][] entities = {
            {"Natural Hairy & Beauty Expo", "2018", "6", "27", "8", "0", "2018", "6", "30", "19", "0", "7318729", "natural-hairy-beauty@gmail.com", "", "A natural hair convention that celebrates, educates & demonstrates natural hair care, techniques and natural hairstyles. Featuring an Afrocentric fashion show, professional hair stylist. The event will feature cosmetics and beauty products, cosmetics, hair accessories, cosmetics, hair accessories, makeup, beauty products, salon and spa equipment etc.", "https://www.youtube.com/watch?v=WI-uJDgvcpc", "https://www.njhairexpo.com/"},
            {"Global Food & Beverage Expo", "2019", "8", "11", "12", "0", "2019", "8", "11", "23", "59", "7094287", "contact@gfba.vegas.com", "", "The first annual Global Food and Beverage Expo is coming to Las Vegas. Las Vegas has grown into the nations top destination for food and beverage. The Strip contributes a total revenue of $660m per year for the food and beverage industry. We would like to welcome you to expand your business in this thriving economy.With 4 conference rooms and exhibitor booths spanning across 171,000 sqft, this show has become the most highly anticipated event of 2019", "", "https://gfba.vegas/"},
            {"World Dairy Expo", "2019", "11", "1", "11", "0", "2019", "11", "5", "16", "0", "76137241", "expo@wordldairyexpo.com", "", "World Dairy Expo is a prominent and an International Trade Show for the Dairy Farmer. It is a focuses on the newest dairy technology and innovations, including animal health supplies, milking systems, feeding products, forage handling and manure equipment plus embryos.", "", "https://worlddairyexpo.com/", ""},
            {"Print Press Conference", "2019", "10", "3", "10", "30", "2019", "10", "5", "23", "59", "7855543", "contact@printpress.com", "", "PRINT is the one place that helps you do just that-offering all the education, tools, products and innovations you need to grow your business and achieve your goals. As the proven event for the entire community of printing and graphics professionals-from creatives to executives, production specialists to sales and marketing teams this is the only industry event tailored to the unique needs of each specialty.", "", "https://theprintevent.com/"},
            {"IECSC Conference", "2019", "10", "6", "9", "0", "2019", "10", "7", "20", "0", "7010091", "iecsc@gmail.com", "", "International Esthetics, Cosmetics & SPA Conference is the leading spa and wellness event in the southeast. Here you will have the opportunity to purchase products, see the latest trends and learn the newest techniques emerging in the spa and wellness market. It is focusing on Spa Business & Wellness, Makeup and Medical Spa Education, Additional business building classes. Spa and wellness professionals rely on IECSC for insightful guidance to grow their businesses and keep them at the forefront of their competitive industry.", "", "https://www.iecsc.com/"}
    };

    /**
     * Seeds the database with events.
     */
    @Override
    public void seed() {
        for (String[] eventData : entities) {
            Calendar calendar = Calendar.getInstance();

            Event event = new Event();
            event.name = eventData[0];

            calendar.set(Integer.parseInt(eventData[1]), Integer.parseInt(eventData[2]), Integer.parseInt(eventData[3]), Integer.parseInt(eventData[4]), Integer.parseInt(eventData[5]));
            event.startDate = calendar.getTime();

            calendar.set(Integer.parseInt(eventData[6]), Integer.parseInt(eventData[7]), Integer.parseInt(eventData[8]), Integer.parseInt(eventData[9]), Integer.parseInt(eventData[10]));
            event.endDate = calendar.getTime();

            event.contactNumber = eventData[11];
            event.mail = eventData[12];
            event.picturePath = eventData[13];
            event.description = eventData[14];
            event.youtubeUrl = eventData[15];
            event.homePage = eventData[16];

            DatabaseManager.getInstance().getDatabase().eventDao().insert(event);
        }
    }
}
