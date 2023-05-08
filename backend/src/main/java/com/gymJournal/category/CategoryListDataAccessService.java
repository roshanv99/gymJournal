package com.gymJournal.category;

import com.gymJournal.member.Gender;
import com.gymJournal.member.Member;

import java.util.ArrayList;
import java.util.List;

public class CategoryListDataAccessService implements CategoryDao{

    private static final List<Category> categories;
    static {
        categories = new ArrayList<>();

        Category bicep = new Category(
                1001,
                "Bicep",
                "f7f7f7",
                "");
        categories.add(bicep);

        Category tricep = new Category(
                1002,
                "Tricep",
                "3f3f3f",
                "Helloooooo"
                );
        categories.add(tricep);
    }

    @Override
    public List<Category> selectAllCategories() {
        return categories;
    }
}
