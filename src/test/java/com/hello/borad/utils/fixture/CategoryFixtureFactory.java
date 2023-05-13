package com.hello.borad.utils.fixture;

import com.hello.borad.domain.board.entity.Category;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.ArrayList;
import java.util.List;

import static org.jeasy.random.FieldPredicates.*;

public class CategoryFixtureFactory {

    public static Category create(String title, int depth, int sequence) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Category.class));

        var titlePredicate = named("title")
                .and(ofType(String.class))
                .and(inClass(Category.class));

        var depthPredicate = named("depth")
                .and(ofType(int.class))
                .and(inClass(Category.class));

        var sequencePredicate = named("sequence")
                .and(ofType(int.class))
                .and(inClass(Category.class));

        var hasPostPredicate = named("hasPost")
                .and(ofType(boolean.class))
                .and(inClass(Category.class));

        var parentCategoryPredicate = named("parentCategory")
                .and(ofType(Category.class))
                .and(inClass(Category.class));

        var childCategoriesPredicate = named("childCategories")
                .and(ofType(List.class))
                .and(inClass(Category.class));

        var postsPredicate = named("posts")
                .and(ofType(List.class))
                .and(inClass(Category.class));

        var param = new EasyRandomParameters()
                .randomize(idPredicate, () -> null)
                .randomize(titlePredicate, () -> title)
                .randomize(depthPredicate, () -> depth)
                .randomize(sequencePredicate, () -> sequence)
                .randomize(hasPostPredicate, () -> false)
                .randomize(parentCategoryPredicate, () -> null)
                .randomize(childCategoriesPredicate, ArrayList::new)
                .randomize(postsPredicate, ArrayList::new);

        return new EasyRandom(param).nextObject(Category.class);
    }

    public static Category create(String title, int depth, int sequence, Category parentCategory) {
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Category.class));

        var titlePredicate = named("title")
                .and(ofType(String.class))
                .and(inClass(Category.class));

        var depthPredicate = named("depth")
                .and(ofType(int.class))
                .and(inClass(Category.class));

        var sequencePredicate = named("sequence")
                .and(ofType(int.class))
                .and(inClass(Category.class));

        var hasPostPredicate = named("hasPost")
                .and(ofType(boolean.class))
                .and(inClass(Category.class));

        var parentCategoryPredicate = named("parentCategory")
                .and(ofType(Category.class))
                .and(inClass(Category.class));

        var childCategoriesPredicate = named("childCategories")
                .and(ofType(List.class))
                .and(inClass(Category.class));

        var postsPredicate = named("posts")
                .and(ofType(List.class))
                .and(inClass(Category.class));

        var param = new EasyRandomParameters()
                .randomize(idPredicate, () -> null)
                .randomize(titlePredicate, () -> title)
                .randomize(depthPredicate, () -> depth)
                .randomize(sequencePredicate, () -> sequence)
                .randomize(hasPostPredicate, () -> false)
                .randomize(parentCategoryPredicate, () -> parentCategory)
                .randomize(childCategoriesPredicate, ArrayList::new)
                .randomize(postsPredicate, ArrayList::new);

        return new EasyRandom(param).nextObject(Category.class);
    }
}
