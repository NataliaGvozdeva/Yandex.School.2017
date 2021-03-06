package com.iamkatrechko.yandexschool2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Главная активность приложения
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class MainActivity extends AppCompatActivity {

    /** Вью пейджер основных страниц-фрагментов */
    private ViewPager mViewPager;
    /** Адаптер для основного вью пейджера */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /** Шапка с вкладками для вью пейджера */
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(SectionsPagerAdapter.pages.TRANSLATE.ordinal()).setIcon(R.drawable.drawable_translate);
        mTabLayout.getTabAt(SectionsPagerAdapter.pages.HISTORY.ordinal()).setIcon(R.drawable.drawable_history);
        mTabLayout.getTabAt(SectionsPagerAdapter.pages.SETTINGS.ordinal()).setIcon(R.drawable.drawable_settings);
    }

    /** Адаптер страниц главного экрана */
    private static class SectionsPagerAdapter extends FragmentPagerAdapter {

        /** Возможные экраны адаптера */
        enum pages {
            /** Экран перевода текста */
            TRANSLATE,
            /** Экран со списком истории */
            HISTORY,
            /** Экран настроек */
            SETTINGS
        }

        /**
         * Конструктор
         * @param fm менеджер фрагментов
         */
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (pages.values()[position]) {
                case TRANSLATE:
                    return TranslateFragment.newInstance();
                case HISTORY:
                    return HistoryFragment.newInstance();
                case SETTINGS:
                    return SettingsFragment.newInstance();
                default:
                    return TranslateFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return pages.values().length;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
