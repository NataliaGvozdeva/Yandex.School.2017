package com.iamkatrechko.yandexschool2017.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamkatrechko.yandexschool2017.R;
import com.iamkatrechko.yandexschool2017.database.HistoryDatabaseHelper.HistoryRecordCursor;

/**
 * Адаптер списка записей истории переводов
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class HistoryCursorAdapter extends RecyclerView.Adapter<HistoryCursorAdapter.ViewHolder> {

    /** Контекст */
    private Context mContext;
    /** Список записей истории переводов */
    private HistoryRecordCursor mCursor;
    /** Слушатель нажатий на элементы списка истории */
    private OnClickListener mClickListener;

    /**
     * Конструктор
     * @param context контекст
     */
    public HistoryCursorAdapter(Context context) {
        mContext = context;
    }

    /**
     * Установить данные списка истории в адаптер
     * @param cursor данные списка истории переводов
     */
    public void setDataCursor(HistoryRecordCursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    /**
     * Устанавливает слушатель нажатий на элементы списка
     * @param listener слушатель нажатий на элементы списка
     */
    public void setClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_item_history_record, parent, false));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // Устанавливаем декоратор-разделитель элементов списка
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindView(mCursor);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    /** Вью холдер для отображения записей списка истории переводов */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** Изображение закладки */
        private ImageView mImageViewBookmark;
        /** Текстовая метка с исходным текстом перевода */
        private TextView mTextViewSourceText;
        /** Текстовая метка с конечным текстом перевода */
        private TextView mTextViewTranslateText;
        /** Текстовая метка с языками перевода */
        private TextView mTextViewLanguages;

        /**
         * Конструктор
         * @param itemView виджет элемента списка
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mImageViewBookmark = (ImageView) itemView.findViewById(R.id.image_view_bookmark);
            mTextViewSourceText = (TextView) itemView.findViewById(R.id.text_view_source_text);
            mTextViewTranslateText = (TextView) itemView.findViewById(R.id.text_view_translate_text);
            mTextViewLanguages = (TextView) itemView.findViewById(R.id.text_view_from_to_lang);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mCursor.moveToPosition(getAdapterPosition());
                        mClickListener.onItemClickListener(mCursor.getID());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mClickListener != null) {
                        mCursor.moveToPosition(getAdapterPosition());
                        mClickListener.onItemLongClickListener(view, mCursor.getID());
                    }
                    return true;
                }
            });

            mImageViewBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mCursor.moveToPosition(getAdapterPosition());
                        mClickListener.onBookmarkClickListener(mCursor.getID(), mCursor.isFavorite());
                    }
                }
            });
        }

        /**
         * Настраивает виджет элемента списка
         * @param cursor запись списка истории
         */
        public void bindView(HistoryRecordCursor cursor) {
            mImageViewBookmark.setColorFilter(ContextCompat.getColor(mContext,
                    cursor.isFavorite() ? R.color.text_color_list_history_bookmark_on : R.color.text_color_list_history_bookmark_off));
            mImageViewBookmark.setImageResource(cursor.isFavorite() ? R.drawable.ic_star_white : R.drawable.ic_star_border_white);
            mTextViewSourceText.setText(cursor.getSource());
            mTextViewTranslateText.setText(cursor.getTranslate());
            mTextViewLanguages.setText(mContext.getString(R.string.languages, cursor.getFromLanguage(), cursor.getToLanguage()).toUpperCase());
        }
    }

    /** Слушатель нажатий на элементы списка истории */
    public interface OnClickListener {

        /**
         * Нажатие на элемент списка истории
         * @param id идентификатор нажатого элемента
         */
        void onItemClickListener(long id);

        /**
         * Долгое нажатие на элемент списка истории
         * @param v  виджет, на котором произошло действие
         * @param id идентификатор нажатого элемента
         */
        void onItemLongClickListener(View v, long id);

        /**
         * Нажатие на иконку закладки элемента списка
         * @param id         идентификатор элемента
         * @param isFavorite текущее состояние избранности
         */
        void onBookmarkClickListener(long id, boolean isFavorite);
    }
}
