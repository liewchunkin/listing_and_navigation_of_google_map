# listing_and_navigation_of_google_map

1.In the CreateMap Activity, the user can search for the specific location by entering
the location name using the search view. Alternatively, the user can tap on the top
right corner to move the camera of google map to the user&#39;s current location. After
that, they can long press to pin on the map. These two ways will return the latitude
and longitude coordinates of the location to InsertFacility Activity.

2.In the InsertFacility Activity, users can type the facility name, facility description
and service list. Users need to tap on the LOCATION button to navigate to the
InsertFacility Activity to fill in the latitude and longitude. They need to tap the
CHOOSE PHOTO button to upload a photo and will display it in an ImageView. All
inputs are properly validated. Finally, the user needs to tap the ADD button to add
the facility. The data entered by the user will be stored in the firebase and room local
database.

In the Maps Activity, it will pin a marker for the specific facility which the user selects
from the recycler view in Nearby Activity. When the user taps on the marker, the
marker will show the facility name and facility description.

In the Maps Activity2, it will pin all the markers for all the available facilities from the
database. It will also pin a marker for the user&#39;s current location. Red marker
represents the facility and the blue marker represents the user&#39;s current location.

In the Nearby Activity, there will be a listing of all facilities in a recycler view. When
a user taps one of the facilities, it will navigate to the Maps Activity. There will be a
floating action button at the bottom right corner. When it is a guest, the button will not
show. When the user is logged in, the button will show. This button will navigate to
the InsertFacility Activity.

In the GPSUtils context, this context will be used to enable the GPS of the user
phone. When the user enters the app, if the GPS is not enabled, a dialog will pop up
and ask to enable the gps. If the GPS is enabled when a user enters the app, this
dialog will not show.

In the CustomInfoWindowForGoogleMap context, this context will be used for the
info window for the marker in the google map. It will be used for Maps Activity,
Maps Activity2 when showing the name and description of the facility.

NOTE: Need to insert own Google Maps API key and connect own Firebase 
