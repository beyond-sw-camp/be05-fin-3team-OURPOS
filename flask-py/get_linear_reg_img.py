import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

def get_reg_img(target_area):
    target_area=int(target_area)

    # 데이터 불러오기
    df = pd.read_csv(r'오차_19202122총매출액_예측.csv')

    # 목표 지역 데이터 추출
    selected_data = df[df['행정동_코드'] == target_area][['2019년_매출_금액', '2020년_매출_금액', '2021년_매출_금액', '2022년_매출_금액', '2023년_예측_매출_금액']]

    # 데이터를 연도별로 변형
    selected_data = selected_data.melt(var_name='Year', value_name='Sales_Amount')

    # 'Year' 열을 정수형으로 변환
    selected_data['Year'] = selected_data['Year'].str.extract(r'(\d+)').astype(int)

    # 이력 데이터와 예측 데이터 분리
    historical_data = selected_data[selected_data['Year'] <= 2022]
    predicted_data = selected_data[selected_data['Year'] == 2023]

    # 회귀직선을 위한 범위 설정
    x_range = np.linspace(2019, 2023, 100)

    # 회귀선 계산
    coefficients = np.polyfit(historical_data['Year'], historical_data['Sales_Amount'], 1)
    regression_line = np.poly1d(coefficients)

    # 회귀직선 값 계산
    regression_values = regression_line(x_range)

    # 플로팅
    plt.figure(figsize=(10, 6))

    # 실제 데이터 산점도
    sns.scatterplot(data=selected_data, x='Year', y='Sales_Amount')

    # 회귀직선 그리기 (연한 남색, 대시된 선)
    plt.plot(x_range, regression_values, color='cornflowerblue', linewidth=2, linestyle='--')

    # 예측 데이터 산점도 (회귀직선보다 앞에 위치)
    plt.scatter(predicted_data['Year'], predicted_data['Sales_Amount'], color='red', zorder=5)

    # 회귀직선 주위에 0.5배 ~ 1.5배 영역을 채우기
    plt.fill_between(x_range, 0.5 * regression_values, 1.5 * regression_values, color='cornflowerblue', alpha=0.2)

    # 축 이름, 타이틀, 범례 제거
    plt.xlabel('')
    plt.ylabel('')
    plt.title('')
    plt.legend().remove()

    # x축과 y축에 눈금 추가
    plt.xticks(np.arange(2019, 2024, 1))
    plt.yticks()

    # 배경에 연한 격자 추가
    plt.grid(color='lightgrey', linestyle='-', linewidth=0.5)


    plt.savefig('reg.png', format='png')
    reg_path='reg.png'

    return reg_path
