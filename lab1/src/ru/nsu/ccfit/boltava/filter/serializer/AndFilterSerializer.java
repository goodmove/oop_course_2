package ru.nsu.ccfit.boltava.filter.serializer;

import ru.nsu.ccfit.boltava.filter.IFilter;
import ru.nsu.ccfit.boltava.filter.composite.AndFilter;
import ru.nsu.ccfit.boltava.filter.parser.FilterParser;

import java.util.regex.Pattern;

import static ru.nsu.ccfit.boltava.resources.FilterPatterns.AND_FILTER;

public class AndFilterSerializer implements IFilterSerializer {

    private static final String mFilterPattern = AND_FILTER;

    @Override
    public AndFilter serialize(String filterString) {
        filterString = filterString.trim();
        if (!Pattern.matches(mFilterPattern, filterString)) {
            throw new IllegalArgumentException("Wrong filter format: " + filterString);
        }

        return new AndFilter(FilterParser.getChildren(filterString));
    }

    public String serialize(IFilter filter) {
        AndFilter andFilter = AndFilter.class.cast(filter);
        String result =  filter.getPrefix() + "(";

        for (IFilter child : andFilter.getChildFilters()) {
            result += (" " + FilterSerializerFactory.create(child.getPrefix()).serialize(child));
        }

        return result + " )";
    }


}
