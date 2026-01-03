#!/usr/bin/env python3
"""
Wikipedia Commons'tan en ünlü 500 resmi çeker ve assets klasörüne kaydeder.
"""

import requests
import json
import os
from pathlib import Path
import time
from typing import List, Dict, Any

# Wikipedia Commons API endpoint
WIKI_API = "https://commons.wikimedia.org/w/api.php"

# Her movement için çekilecek maksimum resim sayısı
IMAGES_PER_MOVEMENT = 30

# Ünlü sanatçılar ve eserleri (manuel kürasyon)
FAMOUS_ARTWORKS = {
    "Cubism": [
        {"artist": "Pablo Picasso", "title": "Les Demoiselles d'Avignon", "year": "1907"},
        {"artist": "Pablo Picasso", "title": "Guernica", "year": "1937"},
        {"artist": "Pablo Picasso", "title": "The Weeping Woman", "year": "1937"},
        {"artist": "Georges Braque", "title": "Houses at L'Estaque", "year": "1908"},
        {"artist": "Juan Gris", "title": "Portrait of Pablo Picasso", "year": "1912"},
    ],
    "Impressionism": [
        {"artist": "Claude Monet", "title": "Impression, Sunrise", "year": "1872"},
        {"artist": "Claude Monet", "title": "Water Lilies", "year": "1906"},
        {"artist": "Pierre-Auguste Renoir", "title": "Bal du moulin de la Galette", "year": "1876"},
        {"artist": "Edgar Degas", "title": "The Dance Class", "year": "1874"},
        {"artist": "Camille Pissarro", "title": "Boulevard Montmartre", "year": "1897"},
    ],
    "Post-Impressionism": [
        {"artist": "Vincent van Gogh", "title": "The Starry Night", "year": "1889"},
        {"artist": "Vincent van Gogh", "title": "Sunflowers", "year": "1888"},
        {"artist": "Paul Cézanne", "title": "The Card Players", "year": "1890"},
        {"artist": "Paul Gauguin", "title": "Where Do We Come From", "year": "1897"},
    ],
    "Surrealism": [
        {"artist": "Salvador Dalí", "title": "The Persistence of Memory", "year": "1931"},
        {"artist": "René Magritte", "title": "The Son of Man", "year": "1964"},
        {"artist": "Joan Miró", "title": "The Tilled Field", "year": "1923"},
    ],
    "Abstract Expressionism": [
        {"artist": "Jackson Pollock", "title": "Number 1", "year": "1950"},
        {"artist": "Mark Rothko", "title": "Orange and Yellow", "year": "1956"},
        {"artist": "Willem de Kooning", "title": "Woman I", "year": "1952"},
    ],
    "Renaissance": [
        {"artist": "Leonardo da Vinci", "title": "Mona Lisa", "year": "1503"},
        {"artist": "Michelangelo", "title": "The Creation of Adam", "year": "1512"},
        {"artist": "Raphael", "title": "The School of Athens", "year": "1511"},
        {"artist": "Sandro Botticelli", "title": "The Birth of Venus", "year": "1485"},
    ],
    "Baroque": [
        {"artist": "Caravaggio", "title": "The Calling of St Matthew", "year": "1600"},
        {"artist": "Rembrandt", "title": "The Night Watch", "year": "1642"},
        {"artist": "Diego Velázquez", "title": "Las Meninas", "year": "1656"},
        {"artist": "Peter Paul Rubens", "title": "The Descent from the Cross", "year": "1614"},
    ],
    "Romanticism": [
        {"artist": "Eugène Delacroix", "title": "Liberty Leading the People", "year": "1830"},
        {"artist": "Caspar David Friedrich", "title": "Wanderer above the Sea of Fog", "year": "1818"},
        {"artist": "J.M.W. Turner", "title": "The Fighting Temeraire", "year": "1838"},
    ],
    "Realism": [
        {"artist": "Gustave Courbet", "title": "The Stone Breakers", "year": "1849"},
        {"artist": "Jean-François Millet", "title": "The Gleaners", "year": "1857"},
    ],
    "Pop Art": [
        {"artist": "Andy Warhol", "title": "Marilyn Diptych", "year": "1962"},
        {"artist": "Roy Lichtenstein", "title": "Whaam!", "year": "1963"},
    ],
}

# Movement kategorileri
MOVEMENT_CATEGORIES = {
    "Renaissance": "Renaissance paintings",
    "Mannerism": "Mannerist paintings",
    "Baroque": "Baroque paintings",
    "Rococo": "Rococo paintings",
    "Neoclassicism": "Neoclassical paintings",
    "Romanticism": "Romantic paintings",
    "Realism": "Realist paintings",
    "Impressionism": "Impressionist paintings",
    "Post-Impressionism": "Post-Impressionist paintings",
    "Art Nouveau": "Art Nouveau paintings",
    "Expressionism": "Expressionist paintings",
    "Cubism": "Cubist paintings",
    "Futurism": "Futurist paintings",
    "Dadaism": "Dada paintings",
    "Surrealism": "Surrealist paintings",
    "Bauhaus": "Bauhaus paintings",
    "Abstract Expressionism": "Abstract expressionist paintings",
    "Pop Art": "Pop art paintings",
    "Minimalism": "Minimalist paintings",
    "Conceptual Art": "Conceptual art",
}


def fetch_category_images(category: str, limit: int = 50) -> List[Dict[str, Any]]:
    """Wikipedia Commons kategorisinden resimleri çeker."""
    params = {
        "action": "query",
        "format": "json",
        "generator": "categorymembers",
        "gcmtitle": f"Category:{category}",
        "gcmtype": "file",
        "gcmlimit": limit,
        "prop": "imageinfo",
        "iiprop": "url|extmetadata|size",
        "iiurlwidth": 800,
    }

    try:
        response = requests.get(WIKI_API, params=params, timeout=30)
        response.raise_for_status()
        data = response.json()

        pages = data.get("query", {}).get("pages", {})
        images = []

        for page_id, page in pages.items():
            if "imageinfo" not in page:
                continue

            imageinfo = page["imageinfo"][0]
            thumburl = imageinfo.get("thumburl") or imageinfo.get("url")

            # .svg ve .tif dosyalarını atla
            if not thumburl or thumburl.endswith(".svg") or ".tif" in thumburl:
                continue

            metadata = imageinfo.get("extmetadata", {})

            # HTML taglerini temizle
            def clean_html(text):
                if not text:
                    return ""
                import re
                return re.sub(r'<[^>]*>', '', str(text.get("value", ""))).strip()

            title = clean_html(metadata.get("ObjectName")) or \
                    page.get("title", "").replace("File:", "").rsplit(".", 1)[0] or \
                    "Untitled"

            artist = clean_html(metadata.get("Artist")) or "Unknown"

            images.append({
                "id": page_id,
                "title": title,
                "artist": artist,
                "year": clean_html(metadata.get("DateTimeOriginal")),
                "imageUrl": thumburl,
                "fullUrl": imageinfo.get("url"),
                "width": imageinfo.get("width", 0),
                "height": imageinfo.get("height", 0),
                "wikiUrl": f"https://commons.wikimedia.org/wiki/{page.get('title', '').replace(' ', '_')}"
            })

        return images

    except Exception as e:
        print(f"Error fetching {category}: {e}")
        return []


def download_image(url: str, filepath: Path) -> bool:
    """Resmi indirir."""
    try:
        response = requests.get(url, timeout=30, stream=True)
        response.raise_for_status()

        filepath.parent.mkdir(parents=True, exist_ok=True)

        with open(filepath, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)

        return True
    except Exception as e:
        print(f"Error downloading {url}: {e}")
        return False


def main():
    """Ana fonksiyon."""
    print("🎨 Wikipedia Commons'tan sanat eserleri çekiliyor...")

    # Output dizinleri
    output_dir = Path(__file__).parent.parent / "app" / "src" / "main" / "assets"
    images_dir = output_dir / "artworks"
    images_dir.mkdir(parents=True, exist_ok=True)

    all_artworks = []
    artwork_id = 1

    # Her movement için resimleri çek
    for movement_name, category in MOVEMENT_CATEGORIES.items():
        print(f"\n📦 {movement_name} çekiliyor...")

        images = fetch_category_images(category, IMAGES_PER_MOVEMENT)

        for img in images:
            # Dosya adı oluştur (sadece ASCII karakterler)
            safe_filename = f"{artwork_id:04d}.jpg"
            image_path = images_dir / safe_filename

            # Resmi indir (eğer yoksa)
            if not image_path.exists():
                print(f"  ⬇️  İndiriliyor: {img['title'][:50]}...")
                if download_image(img["imageUrl"], image_path):
                    # Başarılı, artwork ekle
                    all_artworks.append({
                        "id": artwork_id,
                        "title": img["title"],
                        "artist": img["artist"],
                        "year": img["year"],
                        "movement": movement_name,
                        "imageFile": safe_filename,
                        "wikiUrl": img["wikiUrl"]
                    })
                    artwork_id += 1
                    time.sleep(0.5)  # Rate limiting

            if artwork_id > 500:  # Maksimum 500 resim
                break

        if artwork_id > 500:
            break

        time.sleep(1)  # Rate limiting

    # JSON olarak kaydet
    json_path = output_dir / "artworks.json"
    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump(all_artworks, f, indent=2, ensure_ascii=False)

    print(f"\n✅ Toplam {len(all_artworks)} eser başarıyla indirildi!")
    print(f"📁 Resimler: {images_dir}")
    print(f"📄 Metadata: {json_path}")


if __name__ == "__main__":
    main()
