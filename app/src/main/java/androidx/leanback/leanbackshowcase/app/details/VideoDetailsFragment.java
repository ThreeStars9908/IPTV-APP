package androidx.leanback.leanbackshowcase.app.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.DetailsFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.cards.CardPresenter;
import androidx.leanback.leanbackshowcase.app.cards.Movie;
import androidx.leanback.leanbackshowcase.app.cards.PicassoBackgroundManager;
import androidx.leanback.leanbackshowcase.app.media.VideoExampleWithExoPlayerActivity;
import androidx.leanback.leanbackshowcase.app.wizard.WizardExampleActivity;
import androidx.leanback.leanbackshowcase.utils.Utils;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SparseArrayObjectAdapter;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class VideoDetailsFragment extends DetailsFragment implements OnItemViewClickedListener {
    private static final String TAG = VideoDetailsFragment.class.getSimpleName();
    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;
    private static final String MOVIE = "Movie";

    private CustomFullWidthDetailsOverviewRowPresenter customFullWidthDetailsOverviewRowPresenter;

    private Movie mSelectedMovie;
    private DetailsRowBuilderTask mDetailsRowBuilderTask;

    private static final long ACTION_PLAY = 1;
    private static final long ACTION_RENT = 2;
    private static final long ACTION_WISHLIST = 3;
    private static final long ACTION_RELATED = 4;
    private Action mActionPlay;
    private Action mActionRent;
    private Action mActionWishList;
    private Action mActionRelated;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customFullWidthDetailsOverviewRowPresenter = new CustomFullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenters());

        PicassoBackgroundManager mPicassoBackgroundManager = new PicassoBackgroundManager(getActivity());
        mSelectedMovie = (Movie) getActivity().getIntent().getSerializableExtra(MOVIE);

        mDetailsRowBuilderTask = (DetailsRowBuilderTask) new DetailsRowBuilderTask().execute(mSelectedMovie);
        mPicassoBackgroundManager.updateBackgroundWithDelay(mSelectedMovie.getCardImageUrl());
        setTitle(getString(R.string.detail_view_title));
        setupEventListeners();
    }
    private void setupEventListeners() {
        setOnItemViewClickedListener(this);
    }
    @Override
    public void onStop() {
        mDetailsRowBuilderTask.cancel(true);
        super.onStop();
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        Action action = (Action) item;
        long id = action.getId();

        if (id ==  ACTION_PLAY){
            Intent intent = new Intent(getActivity(),
                    VideoExampleWithExoPlayerActivity.class);
            intent.putExtra("video_title", mSelectedMovie.getTitle());
            intent.putExtra("video_path", mSelectedMovie.getVideoSourceUrl());
            startActivity(intent);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DetailsRowBuilderTask extends AsyncTask<Movie, Integer, DetailsOverviewRow> {
        @Override
        protected DetailsOverviewRow doInBackground(Movie... params) {
            DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);
            try {
                Bitmap poster = Picasso.get()
                        .load(mSelectedMovie.getCardImageUrl())
                        .resize(Utils.convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH),
                                Utils.convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT))
                        .centerCrop()
                        .get();
                row.setImageBitmap(getActivity(), poster);
            } catch (IOException e) {
                Log.w(TAG, e.toString());
            }
            return row;
        }

        @Override
        protected void onPostExecute(DetailsOverviewRow row) {
            /* 1st row: DetailsOverviewRow */
            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            mActionPlay = new Action(ACTION_PLAY, getString(R.string.action_play));
            mActionRent = new Action(ACTION_RENT, getString(R.string.action_rent));
            mActionWishList = new Action(ACTION_WISHLIST, getString(R.string.action_wishlist));
            mActionRelated = new Action(ACTION_RELATED, getString(R.string.action_related));
            sparseArrayObjectAdapter.set(1, mActionPlay);
            sparseArrayObjectAdapter.set(2, mActionRent);
            sparseArrayObjectAdapter.set(3, mActionWishList);
            sparseArrayObjectAdapter.set(4, mActionRelated);

            row.setActionsAdapter(sparseArrayObjectAdapter);

            /* 2nd row: ListRow */
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            for (int i = 0; i < 10; i++) {
                Movie movie = new Movie();
                if (i == 0) {
                    movie.setCardImageUrl("https://pbs.twimg.com/media/C2Dv8DcVEAAC5B_.jpg");
                    movie.setTitle("Sherlock");
                    movie.setStudio("BBC One");
                } else if (i == 1) {
                    movie.setCardImageUrl("https://frpnet.net/wp-content/uploads/2022/05/kapak.jpg");
                    movie.setTitle("Stranger Things");
                    movie.setStudio("Netflix");
                } else if (i == 2) {
                    movie.setCardImageUrl("https://occ-0-784-1380.1.nflxso.net/dnm/api/v6/E8vDc_W8CLv7-yMQu8KMEC7Rrr8/AAAABQkofIypds_rD7yInykgr059MHPbl0gpMiFZ4mgL5Vuu2JRxvs-7fzdvN97DD0Ir2126xeFWMucX9IZJVmH33Hv1WfUJ4GB86SVY.jpg");
                    movie.setTitle("Black Mirror");
                    movie.setStudio("Netflix");
                } else if (i == 3) {
                    movie.setCardImageUrl("https://m.media-amazon.com/images/M/MV5BMjMzNjUxMDk0MF5BMl5BanBnXkFtZTgwMjY1OTgyNDM@._V1_.jpg");
                    movie.setTitle("Dark");
                    movie.setStudio("Netflix");
                }
                listRowAdapter.add(movie);
            }
            HeaderItem headerItem = new HeaderItem(0, "Related Videos");

            ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
            customFullWidthDetailsOverviewRowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_SMALL);

            classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, customFullWidthDetailsOverviewRowPresenter);
            classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

            ArrayObjectAdapter adapter = new ArrayObjectAdapter(classPresenterSelector);
            /* 1st row */
            adapter.add(row);
            /* 2nd row */
            adapter.add(new ListRow(headerItem, listRowAdapter));
            /* 3rd row */
            //adapter.add(new ListRow(headerItem, listRowAdapter));
            setAdapter(adapter);
        }
    }
}
