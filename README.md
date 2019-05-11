# [LL(1) Compiler](http://tzamaras.com/LL1_Compiler.php) &middot; [![License](https://img.shields.io/github/license/sotostzam/LL1_Compiler.svg)](https://img.shields.io/github/license/sotostzam/LL1_Compiler.svg) [![Release](https://img.shields.io/github/release/sotostzam/LL1_Compiler.svg)](https://img.shields.io/github/release/sotostzam/LL1_Compiler.svg) [![Downloads](https://img.shields.io/github/downloads/sotostzam/LL1_Compiler/total.svg)](https://img.shields.io/github/downloads/sotostzam/LL1_Compiler/total.svg)

This is a LL(1) Parser which includes an integrated graphical user interface. Being still a work in progress, in the future it would be a full LL(1) Compiler. This application is intended for computer science students and autodidacts studying compilers or parsers.

## Demo

![Demo](http://tzamaras.com/images/new_gui.png)

## Table of contents

* [Features](#features)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installing](#installing)
  * [Deployment](#deployment)
  * [Usage](#usage)
* [Author](#author)
* [Contributing](#contributing)
* [Acknowledgments](#acknowledgments)
* [License](#license)

## Features

* Create custom LL(1) grammar rules
* Import and export of grammar rules
* Generate of first sets, follow sets and the parsing table
* Each step is accompanied by visual feedback and explanatory text
* Seamlessly checks if a string is recognized with error reporting

## Getting Started

### Prerequisites

* You will need any OS able to run Java like Windows or Linux
* You must have **[Java JDK 8][java]** installed, which already includes JavaFX
* You will also need an editor or an IDE like **[Eclipse IDE][eclipse]**
* You can use **[e(fx)clipse 3.0][efxclipse]** or newer for eclipse

### Installing

1. Fork this repository on your Github account
2. Clone *your forked repository* to your hard drive with `git clone https://github.com/YOURUSERNAME/LL1_Compiler.git`
3. Change location to that folder
4. Open your favorite IDE/Editor create a new javafx project
5. Manually import your forked repository's files
6. Build and run the application

### Deployment

In order to deploy a runnable .jar file follow these steps:

* Go to file -> Export
* From the Java menu select Runnable JAR file
* Select launch configuration
* Select Export destination
* Double click to run the generated file

### Usage

1) Launch the application
2) Click on the *Import Rules* tab
   * From here you can add your own rules
   * Or select some of the pre-loaded ones
   * Click the *Save rules* button
3) Run the *first, follow and parsing table* algorithms by pressing the corresponding tabs. You are also able to analytically see how each step is generated
4) Now you can analyze an input string whether it matches your grammar rules

## Author

**Sotirios Tzamaras** **&middot;** Development Lead

* [Website](http://tzamaras.com)
* [Github](https://github.com/sotostzam)
* [LinkedIn](https://www.linkedin.com/in/sotiris-tzamaras/)

## Contributing

As an open source software, we would be grateful if you wish to contribute bugfixes and improvements. Read our [CONTRIBUTING](CONTRIBUTING.md) to learn about how to report issues and how to propose bugfixes and improvements.

A special thanks to all of our contributors and testers so far:

* [Angelis Vasilios](http://github.com/ang) (Follow algorithm and Unit testing)

## Acknowledgments

The idea of this application was founded within the framework of the course named "Principles of Languages and Translators", taught by assistant professor Stavros Adam at the university of Ioannina (Former Technological Educational Institute of Epirus).

## License

This project is licensed under the GNU General Public Version 3 License - see the [LICENSE](LICENSE.md) file for details

[java]: https://www.java.com/en/download/
[efxclipse]: https://www.eclipse.org/efxclipse/install.html
[eclipse]: https://www.eclipse.org/downloads/packages/release/2019-03/r/eclipse-ide-enterprise-java-developers
