package com.example.hp.speechify;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.example.hp.speechify.models.RecipeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private ListView listView;
    public static final String key="MyKey";
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.listview);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private class RecipeAdapter extends ArrayAdapter
    {   List<RecipeModel> recipeModelList;
        int resource;
        private LayoutInflater inflater=null;
        public RecipeAdapter(Context context, int resource, List<RecipeModel> objects) {
            super(context, resource, objects);
            this.resource = resource;
            recipeModelList = objects;
            inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null)
                {
                    convertView=inflater.inflate(resource,null);
                }
                 TextView recipe_name=(TextView)convertView.findViewById(R.id.recipe_name);
                recipe_name.setText(recipeModelList.get(position).getName());
                return convertView;
            }
    }




    class JSONTask extends AsyncTask<String,String,List<RecipeModel>>
    {
               Context context;
          public JSONTask(Context context){
          this.context=context;
         }
        @Override
        protected List<RecipeModel> doInBackground(String... params) {
            BufferedReader reader=null;
            HttpURLConnection connection=null;
            try {
                URL url = new URL(params[0]);
                connection=(HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                String line ="";
                StringBuffer buffer=new StringBuffer();
                while((line = reader.readLine())!=null)
                {
                    buffer.append(line);
                }

                String finalJSON=buffer.toString();


               Log.e("Async","doInBackground");
               // return finalJSON;
                JSONObject parentObject=new JSONObject(finalJSON);
                //String status=parentObject.getString("status");
                //Toast.makeText(getApplicationContext(),"status :"+status,Toast.LENGTH_SHORT).show();
                JSONArray parentArray = parentObject.getJSONArray("recipe_data");

                ArrayList<RecipeModel> recipeModelList=new ArrayList<>();
                for(int i=0;i<parentArray.length();i++)
                {
                    RecipeModel recipeModel=new RecipeModel();
                    JSONObject finalObject=parentArray.getJSONObject(i);
                    recipeModel.setId(finalObject.getString("id"));
                    recipeModel.setName(finalObject.getString("name"));
                    JSONArray ingredientDataArray=finalObject.getJSONArray("ingredient_data");
                    ArrayList<RecipeModel.Ingredient> ingredientList=new ArrayList<>();
                    for(int j=0;j<ingredientDataArray.length();j++)
                    {
                        RecipeModel.Ingredient ingredient=new RecipeModel.Ingredient();
                        JSONObject ingredientObject=ingredientDataArray.getJSONObject(j);
                        ingredient.setIngredient_id(ingredientObject.getString("ingredient_id"));
                        ingredient.setIngredient_name(ingredientObject.getString("ingredient_name"));
                        ingredientList.add(ingredient);
                    }
                    recipeModel.setIngredientList(ingredientList);
                    recipeModelList.add(recipeModel);

                }

                return recipeModelList;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();}
            catch (JSONException e) {
                e.printStackTrace();

         } finally {
                if(connection!=null)
                    connection.disconnect();

                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<RecipeModel> result) {
            super.onPostExecute(result);
            //dialog.dismiss();
            if(result != null) {
               Log.e("Async","onPost");
                //Toast.makeText(getBaseContext(),"Working",Toast.LENGTH_LONG);
        RecipeAdapter adapter=new RecipeAdapter(getApplicationContext(),R.layout.row,result);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(result!=null)
                        { List<RecipeModel> recipeModelList=result;
                        ArrayList<RecipeModel.Ingredient> ingredientList=recipeModelList.get(position).getIngredientList();
                     Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                        intent.putExtra("Mykey",ingredientList);

                        Log.e("Async","on click");
                        startActivity(intent);}
                    }
                });

            } else {
                Toast.makeText(getBaseContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new JSONTask(this).execute("http://www.speechify.in/internship/android_task.php");
            //Toast.makeText(getApplicationContext(),"json",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
