package ru.nsu.ccfit.boltava.filter.composite;

import ru.nsu.ccfit.boltava.filter.IFilter;

import java.nio.file.Path;

public class AndFilter extends CompositeFilter {

    @Override
    public boolean check(Path fileName) {
        for (IFilter filter : mChildFilters) {
            if (!filter.check(fileName)) return false;
        }

        return true;
    }

}
