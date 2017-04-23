package com.iamkatrechko.yandexschool2017;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamkatrechko.yandexschool2017.adapter.HistoryCursorAdapter;
import com.iamkatrechko.yandexschool2017.database.HistoryDatabaseHelper.HistoryRecordCursor;
import com.iamkatrechko.yandexschool2017.util.UtilPreferences;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;

/**
 * Фрагмент со списком истории переводов
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Константа для загрузки списка истории переводов */
    private static final int HISTORY_LOADER_ALL = 0;
    /** Константа для загрузки отфильтрованного списка истории переводов */
    private static final int HISTORY_LOADER_SEARCH = 1;
    /** Константа для загрузки избранного списка истории переводов */
    private static final int HISTORY_LOADER_FAVORITE = 2;
    /** Ключ аргумента для передачи текста запроса */
    private static final String ARGUMENT_QUERY = "query";

    /** Виджет списка записей истории переводов */
    private RecyclerView mRecyclerViewHistory;
    /** Адаптер виджета списка истории */
    private HistoryCursorAdapter mHistoryCursorAdapter;
    /** Поле ввода для поиска по истории */
    private EditText mEditTextSearch;
    /** Лэйаут с дополнительной информацией о списке */
    private LinearLayout mLayoutInfo;
    /** Текстовая метка с дополнительной информацией о списке */
    private TextView mTextViewInfo;

    /**
     * Возвращает новый экземпляр фрагмента
     * @return новый экземпляр фрагмента
     */
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerViewHistory = (RecyclerView) v.findViewById(R.id.recycler_view_history);
        mEditTextSearch = (EditText) v.findViewById(R.id.edit_text_history_query);
        mLayoutInfo = (LinearLayout) v.findViewById(R.id.layout_info);
        mTextViewInfo = (TextView) v.findViewById(R.id.text_view_info);

        mHistoryCursorAdapter = new HistoryCursorAdapter(getActivity());
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewHistory.setHasFixedSize(false);
        mRecyclerViewHistory.setAdapter(mHistoryCursorAdapter);
        mHistoryCursorAdapter.setClickListener(new HistoryCursorAdapter.OnClickListener() {

            @Override
            public void onItemClickListener(long id) {

            }

            @Override
            public void onBookmarkClickListener(long id, boolean isFavorite) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Record.COLUMN_IS_FAVORITE, !isFavorite);
                getActivity().getContentResolver().update(Record.buildClipUri(id), contentValues, null, null);
            }
        });

        mEditTextSearch.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);
                if (charSequence.length() == 0) {
                    getLoaderManager().restartLoader(HISTORY_LOADER_ALL, null, HistoryFragment.this);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(ARGUMENT_QUERY, String.valueOf(charSequence));
                getLoaderManager().restartLoader(HISTORY_LOADER_SEARCH, bundle, HistoryFragment.this);
            }
        });

        getLoaderManager().restartLoader(HISTORY_LOADER_ALL, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case HISTORY_LOADER_ALL:
                return new CursorLoader(getActivity(),
                        Record.CONTENT_URI,
                        null, null, null, null);
            case HISTORY_LOADER_SEARCH:
                String queryText = args.getString(ARGUMENT_QUERY);
                return new CursorLoader(getActivity(),
                        Record.CONTENT_URI,
                        null,
                        Record.COLUMN_SOURCE + " LIKE '%" + queryText + "%' OR " +
                                Record.COLUMN_TRANSLATE + " LIKE '%" + queryText + "%'",
                        null,
                        null);
            case HISTORY_LOADER_FAVORITE:
                boolean isOnlyFavorite = UtilPreferences.isShowOnlyFavorite(getActivity());
                String onlyFavoriteQuery = isOnlyFavorite ? Record.COLUMN_IS_FAVORITE + " = 1" : "";
                return new CursorLoader(getActivity(),
                        Record.CONTENT_URI,
                        null,
                        onlyFavoriteQuery,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String infoMessage;
        switch (loader.getId()) {
            case HISTORY_LOADER_ALL:
            case HISTORY_LOADER_FAVORITE:
                infoMessage = getString(R.string.history_empty);
                break;
            case HISTORY_LOADER_SEARCH:
                infoMessage = getString(R.string.history_not_found);
                break;
            default:
                return;
        }
        mHistoryCursorAdapter.setDataCursor(new HistoryRecordCursor(data));
        showInfoMessage(data.getCount() == 0, infoMessage);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_history, menu);

        boolean showOnlyFavorite = UtilPreferences.isShowOnlyFavorite(getActivity());
        MenuItem itemStar = menu.findItem(R.id.action_show_favorites);
        changeToolbarItemIcon(itemStar, showOnlyFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Отображать только избранные записи
            case R.id.action_show_favorites:
                boolean isOnly = UtilPreferences.isShowOnlyFavorite(getActivity());
                changeToolbarItemIcon(item, !isOnly);
                UtilPreferences.setShowOnlyFavorite(getActivity(), !isOnly);
                getLoaderManager().restartLoader(HISTORY_LOADER_FAVORITE, null, HistoryFragment.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Меняет значок ToolBar"а (звездочку) в зависимости от того,
     * отображаются ли только избранные записи, или нет
     * @param itemStar           элемент ToolBar'а
     * @param isOnlyFavoriteShow отображаются только избранные записи
     */
    private void changeToolbarItemIcon(MenuItem itemStar, boolean isOnlyFavoriteShow) {
        if (isOnlyFavoriteShow) {
            itemStar.setIcon(R.drawable.ic_star_white);
        } else {
            itemStar.setIcon(R.drawable.ic_star_border_white);
        }
    }

    /**
     * Отображает текстовое сообщение вместо списка истории
     * @param isShow   флаг отображения сообщения вместо списка
     * @param textInfo текстовая информация
     */
    private void showInfoMessage(boolean isShow, String textInfo) {
        mRecyclerViewHistory.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mLayoutInfo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTextViewInfo.setText(textInfo);
    }
}
