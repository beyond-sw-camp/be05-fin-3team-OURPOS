import folium
import geopandas as gpd
import pandas as pd
from selenium import webdriver
from PIL import Image
import io
from io import BytesIO

def get_map(colname,dong_code):
    # GeoJSON 데이터 불러오기
    seoul_geo = gpd.read_file(r'C:\Users\Playdata\Desktop\our_pos_data\seoul_geojson\seoul_geojson_wgs84.geojson')

    # GeoDataFrame을 pandas DataFrame으로 변환
    seoul_geo_data = pd.DataFrame(seoul_geo)

    # 지도 초기화
    m = folium.Map(
        location=[37.559819, 126.963895],
        zoom_start=11, 
        tiles='cartodbpositron',
        zoom_control=False
    )

    # GeoJson 레이어를 지도에 추가
    folium.GeoJson(
        seoul_geo,
        name='지역구'
    ).add_to(m)

    # Choropleth 레이어를 지도에 추가
    folium.Choropleth(
        geo_data=seoul_geo,
        data=seoul_geo_data,
        columns=['ADSTRD_CD', colname],
        key_on='feature.properties.ADSTRD_CD',
        fill_color='YlOrRd',
        fill_opacity=0.5,
        line_opacity=0.2,
        legend_name=colname,
        bins=8  # 히트맵을 8개의 등개수 구간으로 나누기
    ).add_to(m)


    highlight_geo = seoul_geo[seoul_geo['ADSTRD_CD'] == dong_code]

    # 특정 행정동의 테두리를 강조하는 GeoJson 레이어 추가
    folium.GeoJson(
        highlight_geo,
        name='highlight',
        style_function=lambda x: {'color': 'red', 'weight': 2, 'fillOpacity': 0.1}
    ).add_to(m)

    img_data = m._to_png(delay=5)
    img = Image.open(io.BytesIO(img_data))
    img_path = 'map.png'
    img.save(img_path)
    return img_path


def get_user_loc_map(dong_code):
    # GeoJSON 데이터 불러오기
    seoul_geo = gpd.read_file(r'C:\Users\Playdata\Desktop\our_pos_data\seoul_geojson\seoul_geojson_wgs84.geojson')

    # 특정 GeoJSON 객체를 선택
    selected_geo = seoul_geo[seoul_geo['ADSTRD_CD'] == dong_code]

    # 선택한 GeoJSON 객체의 경계 상자 계산
    minx, miny, maxx, maxy = selected_geo.total_bounds
    center_lat = (miny + maxy) / 2
    center_lon = (minx + maxx) / 2

    m = folium.Map(
        location=[center_lat, center_lon],  # 중심 위치 설정
        zoom_start=14,  # 적절한 확대 수준 설정
        tiles='cartodbpositron',
        zoom_control=False
    )

    # GeoJson 레이어를 지도에 추가
    folium.GeoJson(
        selected_geo,
        name='highlight',
        style_function=lambda x: {'color': 'red', 'weight': 2, 'fillOpacity': 0.1}
    ).add_to(m)

    # 레이어 컨트롤(레전드) 추가
    folium.LayerControl().add_to(m)

    img_data = m._to_png(delay=5)
    img = Image.open(io.BytesIO(img_data))
    img_path = 'user_loc_map.png'
    img.save(img_path)
    return img_path


