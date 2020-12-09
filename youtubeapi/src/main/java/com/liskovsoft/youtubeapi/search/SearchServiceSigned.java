package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchTags;
import retrofit2.Call;

import java.util.List;
import java.util.Locale;

/**
 * Wraps result from the {@link SearchManagerSigned}
 */
public class SearchServiceSigned {
    private static final String TAG = SearchServiceSigned.class.getSimpleName();
    private static SearchServiceSigned sInstance;
    private final SearchManagerSigned mSearchManagerSigned;
    private final BrowseServiceSigned mBrowseService;

    private SearchServiceSigned() {
        mSearchManagerSigned = RetrofitHelper.withJsonPath(SearchManagerSigned.class);
        mBrowseService = BrowseServiceSigned.instance();
    }

    public static SearchServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new SearchServiceSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public SearchResult getSearch(String searchText, String authorization) {
        Call<SearchResult> wrapper = mSearchManagerSigned.getSearchResult(SearchManagerParams.getSearchQuery(searchText), authorization);
        SearchResult searchResult = RetrofitHelper.get(wrapper);


        if (searchResult == null) {
            Log.e(TAG, "Empty search result for text %s", searchText);
        }

        return searchResult;
    }

    /**
     * Method uses results from the {@link #getSearch(String, String)} call
     * @return video items
     */
    public SearchResultContinuation continueSearch(String nextSearchPageKey, String authorization) {
        if (nextSearchPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }
        
        Call<SearchResultContinuation> wrapper = mSearchManagerSigned.continueSearchResult(SearchManagerParams.getContinuationQuery(nextSearchPageKey), authorization);
        SearchResultContinuation searchResult = RetrofitHelper.get(wrapper);

        if (searchResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + nextSearchPageKey);
        }

        return searchResult;
    }

    public List<String> getSearchTags(String searchText, String authorization) {
        if (searchText == null) {
            searchText = "";
        }
        Call<SearchTags> wrapper = mSearchManagerSigned.getSearchTags(searchText,
                LocaleManager.instance().getLanguage(),
                LocaleManager.instance().getCountry(),
                mBrowseService.getSuggestToken(authorization),
                authorization);
        SearchTags searchTags = RetrofitHelper.get(wrapper);

        if (searchTags != null && searchTags.getSearchTags() != null) {
            return searchTags.getSearchTags();
        }

        return null;
    }
}
