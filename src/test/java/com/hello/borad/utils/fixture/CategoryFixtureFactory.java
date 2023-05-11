package com.hello.borad.utils.fixture;

import com.hello.borad.domain.board.entity.Category;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

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
                .randomize(parentCategoryPredicate, () -> null)
                .randomize(childCategoriesPredicate, List::of)
                .randomize(postsPredicate, List::of);

        return new EasyRandom(param).nextObject(Category.class);
    }
}
