import matplotlib.pyplot as plt
import pandas as pd
from scipy import stats
import numpy as np
import seaborn as sns

def remove_outliers_iqr(df, column):
    Q1 = df[column].quantile(0.25)
    Q3 = df[column].quantile(0.75)
    IQR = Q3 - Q1
    lower_bound = Q1 - 1.5 * IQR
    upper_bound = Q3 + 1.5 * IQR
    return df[(df[column] >= lower_bound) & (df[column] <= upper_bound)]


def get_err_vis():
    # Load the DataFrame
    df = pd.read_csv(r"C:\Users\Playdata\Desktop\our_pos_data\filtered_sample_rfr_all.csv", encoding="korean")
    df['오차퍼센트'] = df['오차퍼센트'].astype(float)
    '''
    print("전처리 전 오차퍼센트의 통계량")
    print(df["오차퍼센트"].describe())
    '''
    df['오차퍼센트']=np.log(df['오차퍼센트'])
    # Remove outliers
    df_filtered = remove_outliers_iqr(df, '오차퍼센트')

    #print("전처리 후 오차퍼센트의 통계량")
    #print(df_filtered["오차퍼센트"].describe())


    mean = np.mean(df_filtered['오차퍼센트'])
    std = np.std(df_filtered['오차퍼센트'])

    # 역변환된 평균과 신뢰 구간 계산
    mean_exp = np.exp(mean)
    conf_interval_upper = np.exp(mean + 1.96 * std)
    conf_interval_lower = np.exp(mean - 1.96 * std)

    # 결과 출력
    '''
    print("역변환된 평균:", mean_exp)
    print("역변환된 95% 신뢰 구간 상한:", conf_interval_upper)
    print("역변환된 95% 신뢰 구간 하한:", conf_interval_lower)
    '''


    # Plot histogram
    sns.histplot(df_filtered['오차퍼센트'], bins=50, color='black', edgecolor='black')
    plt.xlabel('error')
    plt.ylabel('Frequency')
    plt.savefig('err_hist.png', format='png')
    err_hist_path = 'err_hist.png'


    # QQ plot 추가
    plt.figure()
    stats.probplot(df_filtered['오차퍼센트'], dist="norm", plot=plt)
    plt.title('')
    plt.savefig('qq.png', format='png')
    qq_path='qq.png'

    # shapiro-wilk 테스트 수행
    shapiro_test = stats.shapiro(df_filtered['오차퍼센트'])

    '''
    print("Shapiro-Wilk Test 결과:")
    print("Statistic:", shapiro_test[0])
    print("p-value:", shapiro_test[1])
    '''

    return err_hist_path, qq_path, shapiro_test
