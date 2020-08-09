# 500px Photos Pilot

This is a pilot Android application that uses the [500px](https://500px.com) Photos API.

### Design
This photos app is designed and built using the MVVM architecture, with
the following components:

- GalleryFragment (View, list), with Observers of view model data
  - PhotoGridAdapter, managing the display of Photos
- GalleryViewModel, with LiveData of Photos and categories
- PhotosApiService, a Retrofit service communicating with the Photos API
- DetailFragment (View, item), with Observers of view model data
- DetailViewModel, with LiveData of the selected Photo

### Function

The app requests **Popular**-feature photos from the API by default. From
the gallery view menu, users can switch to **Upcoming** or **Editors' Choice**
feature photos as well.

To view more photos, users can simply scroll down on the gallery view.

Additionally, photo categories present in the gallery view are
automatically extracted and displayed as filter chips. Users can then
choose a filter to view only photos belonging to that category.
