package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.sharedutils.locale.LocaleUtility;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchTags;
import retrofit2.Call;

import java.util.List;
import java.util.Locale;

/**
 * Wraps result from the {@link SearchManagerUnsigned}
 */
public class SearchServiceUnsigned {
    private static SearchServiceUnsigned sInstance;
    private final SearchManagerUnsigned mSearchManagerUnsigned;
    private final Locale mLocale;

    private SearchServiceUnsigned(Locale locale) {
        mLocale = locale;
        mSearchManagerUnsigned = RetrofitHelper.withJsonPath(SearchManagerUnsigned.class);
    }

    public static SearchServiceUnsigned instance(Locale locale) {
        if (sInstance == null) {
            sInstance = new SearchServiceUnsigned(locale);
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public SearchResult getSearch(String searchText) {
        Call<SearchResult> wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(searchText));
        SearchResult searchResult = RetrofitHelper.get(wrapper);


        if (searchResult == null) {
            throw new IllegalStateException("Invalid search result for text " + searchText);
        }

        return searchResult;
    }

    /**
     * Method uses results from the {@link #getSearch(String)} call
     * @return video items
     */
    public SearchResultContinuation continueSearch(String nextSearchPageKey) {
        if (nextSearchPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }
        
        Call<SearchResultContinuation> wrapper = mSearchManagerUnsigned.continueSearchResult(SearchManagerParams.getContinuationQuery(nextSearchPageKey));
        SearchResultContinuation searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + nextSearchPageKey);
        }

        return searchResult;
    }

    public List<String> getSearchTags(String searchText) {
        if (searchText == null) {
            searchText = "";
        }

        Call<SearchTags> wrapper = mSearchManagerUnsigned.getSearchTags(searchText,
                mLocale.getLanguage(),
                mLocale.getCountry());
        SearchTags searchTags = RetrofitHelper.get(wrapper);

        if (searchTags != null && searchTags.getSearchTags() != null) {
            return searchTags.getSearchTags();
        }

        return null;
    }
}
