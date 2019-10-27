package com.example.a9gesllprov.core;

/**
 * Provides functionality for contact filtering.
 */
public class ContactFilter
{
    /**
     * Tests if a name should be filtered.
     * @param name The name to be checked against the filter.
     * @param filter The filter used for filtering.
     * @return True if name should be filtered by the filter false if not.
     */
    public static boolean shouldBeFiltered(String name, String filter) {
        return !name.toLowerCase().contains(filter.toLowerCase());
    }
}
