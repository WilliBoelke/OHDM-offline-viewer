package de.htwBerlin.ois.FileStructure;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


public class SwipeGesturesHome extends ItemTouchHelper.SimpleCallback
{
    //------------Instance Variables------------


    private final ColorDrawable redBackground;
    private final ColorDrawable greenBackground;
    private LeftSwipeCallback leftSwipeCallback;
    private RightSwipeCallback rightSwipeCallback;
    private RecyclerAdapterLocalMaps mAdapter;
    private ColorDrawable actualIColor;


    //------------Constructors------------

    /**
     * Public Constructor to just implement the LeftSwipe
     * @param adapter
     * @param onLeftSwipe
     */
    public SwipeGesturesHome(RecyclerAdapterLocalMaps adapter, LeftSwipeCallback onLeftSwipe)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        actualIColor = new ColorDrawable(Color.GREEN);
        redBackground = new ColorDrawable(Color.RED);
        greenBackground = new ColorDrawable(Color.GREEN);
        this.leftSwipeCallback = onLeftSwipe;
    }

    /**
     * Public Constructor to just implement the RightSwipe
     * @param adapter
     * @param onRightSwipe
     */
    public SwipeGesturesHome(RecyclerAdapterLocalMaps adapter, RightSwipeCallback onRightSwipe)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        actualIColor = new ColorDrawable(Color.GREEN);
        redBackground = new ColorDrawable(Color.RED);
        greenBackground = new ColorDrawable(Color.GREEN);
        this.rightSwipeCallback = onRightSwipe;
    }


    /**
     * Public Constructor to implement both swipe directions
     * @param adapter
     * @param onLeftSwipe
     */
    public SwipeGesturesHome(RecyclerAdapterLocalMaps adapter, RightSwipeCallback onRightSwipe, LeftSwipeCallback onLeftSwipe)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        actualIColor = new ColorDrawable(Color.GREEN);
        redBackground = new ColorDrawable(Color.RED);
        greenBackground = new ColorDrawable(Color.GREEN);
        this.rightSwipeCallback = onRightSwipe;
        this.leftSwipeCallback = onLeftSwipe;
    }


    //------------ItemTouchHelper Methods------------

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1)
    {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();

        if (leftSwipeCallback != null)
        {
            if (direction == ItemTouchHelper.LEFT)
            {
                this.leftSwipeCallback.onLeftSwipe(position);
            }
        }
        if (rightSwipeCallback != null)
        {
            if (direction == ItemTouchHelper.RIGHT)
            {
                this.rightSwipeCallback.onRightSwipe(position);
            }
        }

    }


    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 0;


        // Swiping to the right
        if (dX > 0)
        {
            actualIColor = redBackground;
            actualIColor.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }

        // Swiping to the left
        else if (dX < 0)
        {
            actualIColor = greenBackground;
            actualIColor.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,

                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
        else
        {
            actualIColor.setBounds(0, 0, 0, 0);
        }
        actualIColor.draw(canvas);

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
