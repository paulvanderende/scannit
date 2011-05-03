package nl.javadude.scannit.filter;

import com.google.common.base.Predicate;

import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public abstract class Filter implements Predicate<String> {

    public static Filter include(String regex) {
        return new IncludeFilter(regex);
    }

    public static Filter exclude(String regex) {
        return new ExcludeFilter(regex);
    }

    public static FilterChain chain() {
        return new FilterChain();
    }

    public static FilterChain chain(Filter... filters) {
        FilterChain chain = new FilterChain();
        for (Filter filter : filters) {
            chain.add(filter);
        }
        return chain;
    }

    public static class IncludeFilter extends Filter {
        protected Pattern pattern;

        protected IncludeFilter(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public boolean apply(String match) {
            return pattern.matcher(match).matches();
        }
    }

    public static class ExcludeFilter extends IncludeFilter {
        protected ExcludeFilter(String regex) {
            super(regex);
        }

        @Override
        public boolean apply(String match) {
            return !super.apply(match);
        }
    }

    public static class FilterChain extends Filter {
        private List<Filter> filters = newArrayList();

        public boolean apply(String input) {
            boolean applies = true;
            for (Filter filter : filters) {
                applies = applies && filter.apply(input);
            }
            return applies;
        }

        public FilterChain add(Filter filter) {
            filters.add(filter);
            return this;
        }
    }
}
