/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package androidx.leanback.leanbackshowcase.app.cards;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.details.DetailViewExampleActivity;
import androidx.leanback.leanbackshowcase.app.details.DetailViewExampleFragment;
import androidx.leanback.leanbackshowcase.app.details.DetailsActivity;
import androidx.leanback.leanbackshowcase.app.details.ShadowRowPresenterSelector;
import androidx.leanback.leanbackshowcase.app.room.api.HttpUrlHelper;
import androidx.leanback.leanbackshowcase.cards.presenters.CardPresenterSelector;
import androidx.leanback.leanbackshowcase.models.Card;
import androidx.leanback.leanbackshowcase.models.CardRow;
import androidx.leanback.leanbackshowcase.utils.CardListRow;
import androidx.leanback.leanbackshowcase.utils.Utils;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.DividerRow;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.SectionRow;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonArrayCallback;
import com.studioidan.httpagent.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This fragment will be shown when the "Card Examples" card is selected at the home menu. It will
 * display multiple card types.
 */
public class CardExampleFragment extends BrowseFragment {
    private static final String TAG = CardExampleFragment.class.getSimpleName();
    private ArrayObjectAdapter mRowsAdapter;
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;
    String description = "";

    private static PicassoBackgroundManager picassoBackgroundManager = null;
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setupUi();
//        setupRowAdapter();
        setupUIElements();
        loadRows();
        setupEventListeners();
        picassoBackgroundManager = new PicassoBackgroundManager(getActivity());
        picassoBackgroundManager.updateBackgroundWithDelay("https://pbs.twimg.com/media/EwNSCRIVkAUl9j7.jpg");
    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }
    private void setupUIElements() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.detail_view_related_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.app_guidedstep_actions_background));
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.implement_search),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private static final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            // each time the item is selected, code inside here will be executed.
            if (item instanceof String) {
                // GridItemPresenter
                picassoBackgroundManager.updateBackgroundWithDelay("https://pbs.twimg.com/media/EwNSCRIVkAUl9j7.jpg");
            } else if (item instanceof Movie) {
                // CardPresenter
                picassoBackgroundManager.updateBackgroundWithDelay(((Movie) item).getCardImageUrl());
            }
        }
    }
    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            // each time the item is clicked, code inside here will be executed.
            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                getActivity().startActivity(intent);
            }
//            else if (item instanceof String) {
//                if (item == "ErrorFragment") {
//                    Intent intent = new Intent(getActivity(), ErrorActivity.class);
//                    startActivity(intent);
//                }
//            }
        }
    }
    private void loadRows() {
        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        HttpAgent.get(HttpUrlHelper.getApiUrl(HttpUrlHelper.api_movies))
                .goString(new StringCallback() {
                    @Override
                    protected void onDone(boolean success, String stringResults) {
                        try {
                            JSONArray object_array = new JSONArray(stringResults);
                            for (int nI = 0; nI < object_array.length(); nI ++){
                                JSONObject obj_movie = object_array.getJSONObject(nI);
                                String str_category = obj_movie.getString("category");

                                HeaderItem cardPresenterHeader = new HeaderItem(nI, str_category);
                                CardPresenter cardPresenter = new CardPresenter();
                                ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(cardPresenter);
                                String str_movie_data = obj_movie.getString("data");
                                JSONArray array_items = new JSONArray(str_movie_data);
                                for(int nJ = 0; nJ < array_items.length(); nJ ++){
                                    String str_title = array_items.getJSONObject(nJ).getString("title");
                                    String str_description = array_items.getJSONObject(nJ).getString("desc");
                                    String str_img_url = array_items.getJSONObject(nJ).getString("image");
                                    String str_video_url = array_items.getJSONObject(nJ).getString("path");
                                    Movie movie = new Movie();

                                    movie.setVideoSourceUrl(HttpUrlHelper.getImageUrl(str_video_url));
                                    movie.setCardImageUrl(HttpUrlHelper.getImageUrl(str_img_url));
                                    movie.setTitle(str_title);
                                    movie.setStudio("");
                                    movie.setDescription(str_description);
                                    cardRowAdapter.add(movie);
                                }
                                mRowsAdapter.add(new ListRow(cardPresenterHeader, cardRowAdapter));
                                setAdapter(mRowsAdapter);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        /* Set */

    }

    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setTitle(getString(R.string.card_examples_title));
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.implement_search),
                        Toast.LENGTH_LONG).show();
            }
        });
        setOnItemViewClickedListener(new OnItemViewClickedListener() {

            @Override
            public void onItemClicked(Presenter.ViewHolder viewHolder, Object item, RowPresenter.ViewHolder viewHolder1, Row row) {
                if (!(item instanceof Card)) return;
                if (!(viewHolder.view instanceof ImageCardView)) return;

                ImageView imageView = ((ImageCardView) viewHolder.view).getMainImageView();
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        imageView, DetailViewExampleFragment.TRANSITION_NAME).toBundle();
                Intent intent = new Intent(getActivity().getBaseContext(),
                        DetailViewExampleActivity.class);
                Card card = (Card) item;
                int imageResId = card.getLocalImageResourceId(getContext());
                intent.putExtra(DetailViewExampleFragment.EXTRA_CARD, imageResId);
                startActivity(intent, bundle);
            }

        });

        prepareEntranceTransition();
    }

    private void setupRowAdapter() {
        mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
        setAdapter(mRowsAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 500);
    }

    private void createRows() {

        String json = Utils
                .inputStreamToString(getResources().openRawResource(R.raw.cards_example));
        CardRow[] rows = new Gson().fromJson(json, CardRow[].class);

        for (CardRow row : rows) {
            mRowsAdapter.add(createCardRow(row));
        }
    }

    private Row createCardRow(final CardRow cardRow) {
        switch (cardRow.getType()) {
            case CardRow.TYPE_SECTION_HEADER:
                return new SectionRow(new HeaderItem(cardRow.getTitle()));
            case CardRow.TYPE_DIVIDER:
                return new DividerRow();
            case CardRow.TYPE_DEFAULT:
            default:
                // Build main row using the ImageCardViewPresenter.
                PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
                for (Card card : cardRow.getCards()) {
                    listRowAdapter.add(card);
                }
                return new CardListRow(new HeaderItem(cardRow.getTitle()), listRowAdapter, cardRow);
        }
    }
}
