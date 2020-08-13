<br>

<h1 align="center">Seam Carving</h1>
<div align="center">

[![Badge](https://img.shields.io/badge/Uses-Kotlin-orange.svg?style=flat&logo=kotlin&logoWidth=15&logoColor=orange)](https://kotlinlang.org/)
[![Badge](https://img.shields.io/badge/Made_with-Affection-ff69b4.svg?style=flat&logo=ko-fi&logoWidth=15&logoColor=ff69b4)](https://i.pinimg.com/736x/d7/5f/e3/d75fe32e7af10c3ed0bafb98816a6ce2.jpg)
[![Badge](https://img.shields.io/badge/Open-Source-green.svg?style=flat&logo=open-source-initiative&logoWidth=15&logoColor=green)](https://ru.wikipedia.org/wiki/Open_source)

</div>


## Description
Seam carving is an image processing technique for content aware image resizing.
Content aware means that it saves objects and object aspect ratio at the processed image.  


## Documentation
Seam Carving is util with CLI, which supports the following flags:
*   -action //- type of work with the image or something else, which could be:
    + help //- get help for this util
    + to-energy //- convert image from -in path to -out path with energy filter
    + to-negative //- convert image from -in path to -out path with negative filter
    + seam-carving //- seam-carving -width & -height image from -in path to -out path
*   -in //- input path of existing image
*   -out //- output path of created image
*   -width //- width to remove with seam-carving
*   -height //- height to remove with seam-carving


## Using example
### Transformation to negative
*   Input image:

<div align="center">
    <img src="https://github.com/Ythosa/seam-carving/blob/master/test/input/blue.png" alt="Input image"/>
</div>

*   Command: `java MainKt -action to-negative -in ./test/input/blue.png -out ./test/output/blue_negative.png`
*   Output image:

<div align="center">
    <img src="https://github.com/Ythosa/seam-carving/blob/master/test/output/blue_negative.png" alt="Output image"/>
</div>

### Transformation to energy
*   Input image:

<div align="center">
    <img src="https://github.com/Ythosa/seam-carving/blob/master/test/input/tree.png" alt="Input image"/>
</div>

*   Command: `java MainKt -action to-energy -in ./test/input/tree.png -out ./test/output/tree_energy.png`
*   Output image:

<div align="center">
    <img src="https://github.com/Ythosa/seam-carving/blob/master/test/output/tree_energy.png" alt="Output image"/>
</div>

## FAQ
_Q_: How can I help to develop this project?  
_A_: You can put a :star: :3

<br>

<div align="center">
  Copyright 2020 Ythosa
</div>
