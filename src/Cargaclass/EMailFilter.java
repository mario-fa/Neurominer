package Cargaclass;

import java.util.Locale;

public class EMailFilter
{
    /**
     * The filter
     */
    private static String [] filter;

    /**
     * @return the filter
     */
    public static String[] getFilter()
    {
        return filter;
    }

    /**
     * @param aFilter the filter to set
     */
    public static void setFilter(String[] aFilter) 
    {
        filter = aFilter;
    }

    public static String Filter(String message)
    {        
        if (filter.length == 0) // No Filter
            return  message;
        else
        {
            String currentFilter;
            int endIndex;

            for (int i = 0; i < filter.length; i++)
            {
                String message_in_upper = message.toUpperCase(Locale.getDefault());
                currentFilter = filter[i].toUpperCase();
                endIndex = message_in_upper.indexOf(currentFilter);
                if (endIndex != -1) // exists
                    message = message.substring(0, endIndex);
            }
            
            return  message;
        }
    }
}
