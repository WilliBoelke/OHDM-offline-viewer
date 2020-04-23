package de.htwBerlin.ois.FileStructure;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import de.htwBerlin.ois.FTP.FtpTaskFileDownloading;
import de.htwBerlin.ois.R;


public class OhdmFileSwipeToDownloadCallback extends ItemTouchHelper.SimpleCallback
{
    private OhdmFileRecyclerAdapter mAdapter;
    private final ColorDrawable redBackground;
    private final ColorDrawable greenBackground;
    private ColorDrawable actualIColor;

    public OhdmFileSwipeToDownloadCallback(OhdmFileRecyclerAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;

        actualIColor  = new ColorDrawable(Color.GREEN);
        redBackground = new ColorDrawable(Color.RED);
        greenBackground = new ColorDrawable(Color.GREEN);

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        if(direction == ItemTouchHelper.LEFT)
        {
            //TODO Download

                //FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(progressBar, context);
                //Toast.makeText(mAdapter.getContext(), "Downloading " + fileName, Toast.LENGTH_SHORT).show();
                //ftpTaskFileDownloading.execute(ohdmFile);
                //disableButton(buttonDownloadFile);

        }
        else  if(direction == ItemTouchHelper.RIGHT)
        {
            //DODO delete
        }

    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 0;


        // Swiping to the right
        if (dX > 0)
        {

            actualIColor = redBackground;

            actualIColor.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());

        }
        // Swiping to the left
        else if (dX < 0)
        {
            actualIColor = greenBackground;



            actualIColor.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,

                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

        }
        // view is unSwiped
        else
        {

            actualIColor.setBounds(0, 0, 0, 0);

        }

        actualIColor.draw(c);
        // actualIcon.draw(c);
    }

}