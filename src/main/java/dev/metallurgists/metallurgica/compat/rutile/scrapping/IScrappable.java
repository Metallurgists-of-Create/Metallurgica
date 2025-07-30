package dev.metallurgists.metallurgica.compat.rutile.scrapping;

import dev.metallurgists.rutile.api.material.base.Material;

public interface IScrappable {
    ScrappingData getScrappingData(Material mainMaterial);
}
