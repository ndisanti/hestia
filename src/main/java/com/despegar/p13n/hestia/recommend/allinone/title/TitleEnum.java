package com.despegar.p13n.hestia.recommend.allinone.title;

import java.util.EnumSet;

import com.despegar.p13n.euler.commons.client.model.Product;


public enum TitleEnum {


    /**Oferta de {@code<producto>} en {@code<IATA>} */
    T1(true, true, false), //

    /**Aproveche estas ofertas de {@code<producto>} en {@code<IATA>} */
    T2(true, true, false),

    /**Aproveche estas ofertas de {@code<producto>}*/
    T3(true, false, false),

    /**Oferta de {@code<producto>} a {@code<IATA>} */
    T4(true, true, false),

    /**Aproveche estas ofertas de {@code<producto>} a {@code<IATA>} */
    T5(true, true, false),

    /**Aproveche estas ofertas de {@code<producto>} saliendo desde {@code<IATA>} */
    // only for FLIGHTS and closed packages
    T6(true, false, true, EnumSet.of(Product.FLIGHTS, Product.CLOSED_PACKAGES)),

    /**Destinos principales */
    T7(false, false, false),

    /**Hoteles y vuelos */
    T8(false, false, false),

    /**Ofertas solo por hoy */
    T9(false, false, false),

    /**Mas buscados */
    T10(true, false, false),

    /**Hoteles en Destinos Internacionales*/
    T11(false, false, false, EnumSet.of(Product.HOTELS)),

    /**Hoteles en Destinos Nacionales*/
    T12(false, false, false, EnumSet.of(Product.HOTELS)),

    /**Hoteles en oferta*/
    T13(false, false, false, EnumSet.of(Product.HOTELS)),

    /**Destinos cercanos a {@code<IATA>} para su viaje*/
    T14(false, true, false),

    /** Pasajes Aéreos Internacionales*/
    T15(false, false, false, EnumSet.of(Product.FLIGHTS)),

    /** Pasajes Aéreos Nacionales*/
    T16(false, false, false, EnumSet.of(Product.FLIGHTS)),

    /** Paquetes Turísticos Internacionales*/
    T18(false, false, false, EnumSet.of(Product.CLOSED_PACKAGES)),

    /** Paquetes Turísticos Nacionales*/
    T19(false, false, false, EnumSet.of(Product.CLOSED_PACKAGES)),

    /** Alquiler de Autos en Destinos Internacionales*/
    T21(false, false, false, EnumSet.of(Product.CARS)),

    /** Alquiler de Autos en Destinos Nacionales*/
    T22(false, false, false, EnumSet.of(Product.CARS)),

    /** Alquiler de autos económicos*/
    T23(false, false, false, EnumSet.of(Product.CARS)),

    /** Cruceros a las mejores regiones*/
    T24(false, false, false, EnumSet.of(Product.CRUISES)),

    /** Ofertas de cruceros */
    T25(false, false, false, EnumSet.of(Product.CRUISES)),

    /** Ofertas de {@code<producto>} */
    T26(true, false, false),

    /** Vuelos en oferta */
    T27(false, false, false, EnumSet.of(Product.FLIGHTS)),

    /** Paquetes turisticos en oferta */
    T28(false, false, false, EnumSet.of(Product.CLOSED_PACKAGES)),

    /** Hoteles en los mejores destinos */
    T29(false, false, false, EnumSet.of(Product.HOTELS)),

    /** Descuentos de {@code<producto>} en {@code<IATA>}*/
    T30(true, true, false),

    /** Disfrute estas ofertas de {@code<producto>} en {@code<IATA>}*/
    T31(true, true, false),

    /** Disfrute estas ofertas de {@code<producto>} */
    T32(true, false, false),

    /** Descuentos de {@code<producto>} a {@code<IATA>}*/
    T33(true, true, false),

    /** Disfrute estos descuentos de {@code<producto>} [saliendo desde {@code<IATA>} origen]*/
    T34(true, false, true),

    /** Disfrute estas ofertas de {@code<producto>} [saliendo desde {@code<IATA>} origen]*/
    T35(true, false, true),

    /** Principales rumbos */
    T36(false, false, false),

    /** Descuentos solo por hoy */
    T38(false, false, false),

    /** Mas solicitados */
    T39(false, false, false),

    /** Rebajas en {@code<producto>} en {@code<IATA>}  */
    T40(true, true, false),

    /** Consiga estos descuentos de {@code<producto>} en {@code<IATA>}  */
    T41(true, true, false),

    /** Consiga estos descuentos de {@code<producto>}  */
    T42(true, false, false),

    /** Consiga estos descuentos de {@code<producto>} a {@code<IATA>} [saliendo desde {@code<IATA>} origen] */
    T43(true, true, true),

    /** Consiga estos descuentos de {@code<producto>} [saliendo desde {@code<IATA>} origen] */
    T44(true, false, true),

    /** Rebajas sólo por hoy*/
    T45(false, false, false),

    /** Mas visitados*/
    T46(false, false, false),

    /** Alquileres temporales en oferta */
    T47(false, false, false),

    /** Tickets Disney */
    T48(false, false, false, EnumSet.of(Product.ACTIVITIES)),

    /**Tickets parques temáticos Orlando*/
    T49(false, false, false, EnumSet.of(Product.ACTIVITIES)),

    /**Actividades y Atracciones en destinos internacionales*/
    T50(false, false, false, EnumSet.of(Product.ACTIVITIES)),

    /**Actividades y Atracciones en destinos nacionales*/
    T51(false, false, false, EnumSet.of(Product.ACTIVITIES)),

    /** Cruceros baratos a todo el mundo*/
    T52(false, false, false, EnumSet.of(Product.CRUISES)),

    /** Encontra tu destino ideal en crucero*/
    T53(false, false, false, EnumSet.of(Product.CRUISES)),

    /**Excursiones y entretenimiento en destinos nacionales*/
    T54(false, false, false, EnumSet.of(Product.ACTIVITIES)),

    /**Paseos y entretenimiento en destinos nacionales*/
    T55(false, false, false, EnumSet.of(Product.ACTIVITIES)),

    /** Ofertas en <IATA_DESTINATION>*/
    T56(false, true, false),

    /** Precios increíbles en <IATA_DESTINATION>*/
    T57(false, true, false),

    /** Descuentos en <IATA_DESTINATION>*/
    T58(false, true, false),

    /** Ofertas increibles*/
    T59(false, false, false),

    /** Aproveche estos descuentos*/
    T60(false, false, false),

    /** Disfrute de estas ofertas*/
    T61(false, false, false),

    /** Completá tu viaje aprovechando estas ofertas*/
    T62(false, false, false),

    /** Completá tu viaje con estos precios increíbles*/
    T63(false, false, false),

    /** Completá tu viaje sin perderte estos descuentos*/
    T64(false, false, false),

    /**Vive tu viaje con estas ofertas*/
    T65(false, false, false),

    /**Alquileres Temporarios en destinos internacionales*/
    T66(false, false, false),

    /**Alquileres Temporarios en destinos nacionales*/
    T67(false, false, false),

    /**¡Por qué 16 millones de viajeros nos eligen!*/
    T68(false, false, false),

    /**¿Buscas un <PRODUCT_SINGULAR> para tu estadía en <IATA_DESTINATION>? */
    T69(true, true, false),

    /**Como buscaste <PRODUCT_PLURAL> en <IATA_DESTINATION> te recomendamos estas ofertas */
    T70(true, true, false),

    /**Quienes viajaron a <IATA_DESTINATION> eligieron estas ofertas de <PRODUCT_PLURAL> */
    T71(true, true, false),

    /**Disfruta de <IATA_DESTINATION> con estos descuentos en <PRODUCT_PLURAL> */
    T72(true, true, false),

    /** Viajeros que conocieron <IATA_DESTINATION> se alojaron en estos hoteles*/
    T73(false, true, false),

    /**<PRODUCT_PLURAL> seleccionados especialmente para ti */
    T74(true, false, false),

    /**Encuentra tu <PRODUCT_SINGULAR> a <IATA_DESTINATION> con el mejor precio */
    T75(true, true, false),

    /**Gente que va a viajar a <IATA_DESTINATION> también reservo estos <PRODUCT_PLURAL> */
    T76(true, true, false),

    /**Escapate de <IATA_ORIGIN> con estas ofertas de <PRODUCT_PLURAL> */
    T77(true, false, true),

    /** Personas que viajaron a <IATA_DESTINATION> buscaron <PRODUCT_PLURAL>*/
    T78(true, true, false),

    /** Descubre tu lugar en el mundo con estas ofertas de <PRODUCT_PLURAL> saliendo desde <IATA_ORIGIN>*/
    T79(true, false, true),

    /** Que buscan las personas como tú*/
    T80(false, false, false),

    /**Porque siempre se puede conocer un poco más... */
    T81(false, false, false),

    /**Los que visitaron <IATA_DESTINATION> también visitaron estos destinos */
    T84(false, true, false),

    /**Ya que vas a <IATA_DESTINATION>, aprovecha y conoce...*/
    T85(false, true, false),

    /**¿Te quedas solo en <IATA_DESTINATION>? También podes visitar...*/
    T86(false, true, false),

    /**<IATA_DESTINATION> es un lugar ideal para recorrer en auto*/
    T87(false, true, false),

    /**¿Ya sabes cómo vas a conocer <IATA_DESTINATION>?*/
    T88(false, true, false),

    /**Los <PRODUCT_PLURAL> favoritos de nuestros viajeros */
    T89(true, false, false),

    /**Los <PRODUCT_PLURAL> preferidos por personas como tú */
    T90(true, false, false),

    /**Conoce ciudades asombrosas de una manera diferente  */
    T91(false, false, false),

    /**¡Prueba una nueva forma de alojarte! */
    T92(false, false, false),

    /**Descubre la magia de Disney */
    T93(false, false, false),

    /**Conoce la diversión de los parques de Orlando */
    T94(false, false, false),

    /**No te pierdas nuestras ofertas y escápate a <IATA_DESTINATION>*/
    T95(false, true, false),

    /**Conoce <IATA_DESTINATION> a tu manera */
    T96(false, true, false),

    /**Ahorra y conoce <IATA_DESTINATION> con estos descuentos */
    T97(false, true, false),

    /**¡No te pierdas estos descuentos en <IATA_DESTINATION>! */
    T98(false, true, false),

    /**Estás buscando qué hacer en <IATA_DESTINATION>? */
    T99(false, true, false),

    /**¿Ya sabés qué vas a hacer cuando llegues a <IATA_DESTINATION>? */
    T100(false, true, false),

    /**Personas que viajan a <IATA_DESTINATION> aprovecharon estas ofertas */
    T101(false, true, false),

    /**Explora los mejores precios en <IATA_DESTINATION> */
    T102(false, true, false),

    /**Déjate sorprender por <IATA_DESTINATION> */
    T103(false, true, false),

    /**<IATA_DESTINATION> es siempre una buena idea */
    T104(false, true, false),

    /**<IATA_DESTINATION>, a un click de distancia */
    T105(false, true, false),

    /**¿Qué esperas para conocer <IATA_DESTINATION>? */
    T106(false, true, false),

    /**¡<IATA_DESTINATION> te está esperando! */
    T107(false, true, false),

    /**<IATA_DESTINATION> es un destino a tu alcance */
    T108(false, true, false),

    /**Si no es ahora, ¿cuándo? Escápate a <IATA_DESTINATION> */
    T109(false, true, false),

    /**¡Dile que sí a <IATA_DESTINATION>! */
    T110(false, true, false),

    /**Ten todo listo para tu viaje a <IATA_DESTINATION> */
    T113(false, true, false),

    /**Prepara tu viaje a <IATA_DESTINATION> */
    T114(false, true, false),

    /**Haz de tu viaje a <IATA_DESTINATION> una experiencia única */
    T115(false, true, false),

    /**Conseguimos estas ofertas para tu viaje a <IATA_DESTINATION> */
    T116(false, true, false),

    /**Comienza a planear tu viaje a <IATA_DESTINATION> */
    T117(false, true, false),

    /**Te ayudamos a preparar tu viaje a <IATA_DESTINATION> */
    T118(false, true, false),

    /**Conoce <IATA_DESTINATION> a tu manera */
    T119(false, true, false),

    /**Empieza a disfrutar de <IATA_DESTINATION> con estas ofertas */
    T120(false, true, false),

    /**<IATA_DESTINATION> se disfruta más con estas ofertas */
    T121(false, true, false),

    /**¡Tu viaje a <IATA_DESTINATION> recién comienza! */
    T122(false, true, false),

    /**¿Qué esperas para ir a los lugares que siempre quisiste conocer? */
    T123(false, false, false),

    /**Haz más de lo que te apasiona , ¡Viaja con nuestras ofertas! */
    T124(false, false, false),

    /**Los mejores viajes se miden en recuerdos, ¡Vívelos hoy con estas ofertas! */
    T125(false, false, false),

    /**Lugares que están esperando que los descubras */
    T126(false, false, false),

    /**Elige, conoce y disfruta */
    T127(false, false, false),

    /**Vive el viaje que siempre soñaste */
    T128(false, false, false),

    /**No busques el lugar perfecto para ir, ¡hazlo perfecto! */
    T129(false, false, false),

    /**Ofertas exclusivas para viajeros como tú */
    T130(false, false, false),

    /**Ofertas recomendadas para tu viaje */
    T131(false, false, false),

    /**Tu viaje, nuestras recomendaciones */
    T132(false, false, false),

    /**Todo viaje tiene sorpresas, ¡descúbrelas! */
    T133(false, false, false),

    /**Usted vió */
    ST1(false, false, false),

    /**Otros usuarios vieron */
    ST2(false, false, false);


    private final boolean productRequired;
    private final boolean destinationRequired;
    private final boolean originRequired;
    private final EnumSet<Product> supported;

    private TitleEnum(boolean productRequired, boolean destinationRequired, boolean originRequerid) {
        this(productRequired, destinationRequired, originRequerid, EnumSet.of(Product.FLIGHTS, Product.CARS,
            Product.CRUISES, Product.HOTELS, Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES,
            Product.VACATIONRENTALS));
    }


    private TitleEnum(boolean productRequired, boolean destinationRequired, boolean originRequerid,
        EnumSet<Product> supported) {
        this.productRequired = productRequired;
        this.destinationRequired = destinationRequired;
        this.originRequired = originRequerid;
        this.supported = supported;

    }

    public boolean isProductRequired() {
        return this.productRequired;
    }

    public boolean isDestinationRequired() {
        return this.destinationRequired;
    }

    public boolean isOriginRequired() {
        return this.originRequired;
    }

    public EnumSet<Product> getSupported() {
        return this.supported;
    }
}
