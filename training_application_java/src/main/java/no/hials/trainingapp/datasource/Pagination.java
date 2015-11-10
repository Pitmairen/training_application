package no.hials.trainingapp.datasource;

import no.hials.trainingapp.routing.NotFoundException;

import java.security.InvalidParameterException;

/**
 * Used to generate pagination when listing content from the database
 */
public final class Pagination {

    private int mItemCount;
    private int mItemsPerPage;
    private int mCurrentPage;


    public Pagination(int itemsPerPage, int currentPage) {
        mItemCount = 0;
        mCurrentPage = currentPage;
        setItemsPerPage(itemsPerPage);
    }

    /**
     * Set the total number of items in this listing
     *
     * @param count number of items
     */
    public void setItemCount(int count) {
        mItemCount = count;
        validateValues();
    }

    /**
     * Sets the number of items per page
     *
     * @param itemsPerPage
     */
    public void setItemsPerPage(int itemsPerPage) {
        if (itemsPerPage <= 0) {
            throw new InvalidParameterException("items per page can't be less than 1");
        }
        mItemsPerPage = itemsPerPage;
    }

    /**
     * Sets the current page number
     *
     * @param page
     */
    public void setCurrentPage(int page) {
        mCurrentPage = page;
    }

    /**
     * Returns the number of items to skip when listing the items.
     * (Used in sql queries)
     *
     * @return
     */
    public int getOffset() {
        return (mCurrentPage - 1) * mItemsPerPage;
    }

    /**
     * Returns the number of items to list starting from the offset
     * (Used in sql queries)
     *
     * @return
     */
    public int getLimit() {
        return mItemsPerPage;
    }

    /**
     * Returns the total number of pages
     *
     * @return
     */
    public int getTotalPageCount() {
        return (int) Math.ceil((double) mItemCount / mItemsPerPage);
    }

    /**
     * Returns the curent page
     *
     * @return
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * True if there is a next page
     *
     * @return
     */
    public boolean hasNextPage() {
        return getCurrentPage() < getTotalPageCount();
    }

    /**
     * True if there is a previous page
     *
     * @return
     */
    public boolean hasPreviousPage() {
        return getCurrentPage() > 1;
    }

    /**
     * Returns the next page
     *
     * @return
     */
    public int getNextPage() {
        return getCurrentPage() + 1;
    }

    /**
     * Returns the previous page
     *
     * @return
     */
    public int getPreviousPage() {
        return getCurrentPage() - 1;
    }

    /**
     * Validates the current values. Throws an exception if something is wrong.
     */
    public void validateValues() {
        if (getCurrentPage() < 1 || getCurrentPage() > getTotalPageCount()) {
            throw new NotFoundException("Unknown page number");
        }
    }
}
