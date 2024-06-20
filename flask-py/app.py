from flask import Flask, send_file, jsonify, request
from flask_cors import CORS
import threading

app = Flask(__name__)
CORS(app)

@app.route('/')
def home():
   return 'This is Home!'

# @app.route('/api/v1/calculato') #get echo api
# def get_echo_call():
#     dong_code = request.args.get('dong_code')
#     return jsonify({"dong_code": dong_code})

if __name__ == '__main__':
   app.run('0.0.0.0',port=5000,debug=True)

@app.route('/api/v1/calculator') #get echo api
def load_pdf():
    from io import BytesIO
    dong_code = request.args.get('dong_code')

    print("dong_code", dong_code)

    print("서버 연결 완료")

    # PDF 생성 함수 호출
    create_report(dong_code)
    print("pdf 다운 함수 호출")

    pdf_bytes = BytesIO()
    with open("예상매출액보고서.pdf", "rb") as f:
        pdf_bytes = BytesIO(f.read())
    pdf_bytes.seek(0)
    print("pdf 다운로드 준비 완료")

    return send_file(pdf_bytes, mimetype='application/pdf', as_attachment=True, download_name="예상매출액보고서.pdf")


# PDF 생성 함수
def create_report(dong_code):
    from reportlab.pdfgen import canvas
    from reportlab.pdfbase import pdfmetrics
    from reportlab.pdfbase.ttfonts import TTFont
    from reportlab.lib.pagesizes import letter
    import textwrap
    import os
    from datetime import datetime
    import geo_visualization
    from reportlab.lib.utils import ImageReader
    import visualization_error_from_sample_rfr as err_vis
    import get_linear_reg_img as lin_reg
    import pandas as pd

    cover = r".\cover_2.png"

    target_area= dong_code

    print("pdf 생성 함수 시작")


    fonts_dir = os.path.join(os.path.dirname(__file__), 'fonts')

# 폰트 등록
    pdfmetrics.registerFont(TTFont("맑은고딕", os.path.join(fonts_dir, "malgun.ttf")))

    # 폰트 등록
    pdfmetrics.registerFont(TTFont("맑은고딕", "malgun.ttf"))
    pdfmetrics.registerFont(TTFont("바탕", os.path.join(fonts_dir, "batang.ttc")))
    pdfmetrics.registerFont(TTFont("굴림", os.path.join(fonts_dir, "gulim.ttc")))
    pdfmetrics.registerFont(TTFont("한컴고딕굵게", os.path.join(fonts_dir, "Hancom Gothic Bold.ttf")))
    pdfmetrics.registerFont(TTFont("한컴고딕보통", os.path.join(fonts_dir, "Hancom Gothic Regular.ttf")))


    pdf = canvas.Canvas("예상매출액보고서.pdf", pagesize=letter)
    width, height = letter
    pdf.drawImage(cover, 0, 0, width=width, height=height)


    pdf.setFont("한컴고딕굵게", 30)
    title = "예상 매출액 추정 보고서"
    str_width = pdf.stringWidth(title)
    pdf.setFillColorRGB(0.09019607843137255, 0.1450980392156863, 0.42745098039215684)
    pdf.drawString((width - str_width) / 2, 750, title)
    pdf.setStrokeColorRGB(0.09019607843137255, 0.1450980392156863, 0.42745098039215684)
    pdf.setLineWidth(4)
    pdf.line(30, 730, 580, 730)
    pdf.setLineWidth(0)
    #상단에 주석으로 현재시간 출력
    pdf.setFont("한컴고딕보통", 9)
    now = datetime.now()
    current_time_str = now.strftime("%Y-%m-%d %H:%M:%S")
    pdf.drawString(485, 740, current_time_str)
    #logo=r"C:\Users\Playdata\Desktop\our_pos_data\캡처\our_pos.png"
    #pdf.drawImage(logo, 40, height - 56, width=47, height=47)



    # 예상 매출액

    df=pd.read_csv(r'filtered_sample_rfr_all.csv', encoding="korean")
    exp_sales = df.loc[df['행정동_코드'] == int(target_area), '2023년_예측_매출_금액'].values[0]
    pdf.setFont("한컴고딕보통", 30)
    expected_sales_intro = "예상 연간 매출액은"
    expected_sales_amounts = "{}원~\n{}원입니다.".format(int(exp_sales*0.7),int(exp_sales*1.3))


    ## 그림자 글씨
    # 첫 번째 라인
    pdf.setFillColorRGB(0.9, 0.9, 0.9)
    text_intro = pdf.beginText(41, height - 106)
    text_intro.setFont("굴림", 10)
    text_intro.textLine(expected_sales_intro)
    pdf.drawText(text_intro)

    # 나머지 라인
    pdf.setFillColorRGB(0.9, 0.9, 0.9)
    text_amounts = pdf.beginText(42, height - 142)  # 위치 조정
    text_amounts.setFont("굴림", 25)
    for line in expected_sales_amounts.split("\n"):
        text_amounts.textLine(line)
    pdf.drawText(text_amounts)
    ## 본 글씨
    # 첫 번째 라인
    pdf.setFillColorRGB(0.09019607843137255, 0.1450980392156863, 0.42745098039215684)
    text_intro = pdf.beginText(40, height - 105)
    text_intro.setFont("굴림", 10)
    text_intro.textLine(expected_sales_intro)
    pdf.drawText(text_intro)

    # 나머지 라인
    pdf.setFillColorRGB(0.09019607843137255, 0.1450980392156863, 0.42745098039215684)
    text_amounts = pdf.beginText(40, height - 140)  # 위치 조정
    text_amounts.setFont("굴림", 25)
    for line in expected_sales_amounts.split("\n"):
        text_amounts.textLine(line)
    pdf.drawText(text_amounts)

    # 이미지 배치
    pdf.setFillColorRGB(0,0,0)  # RGB 색상 지정
    pdf.setFillAlpha(0.05)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setStrokeAlpha(0.05)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setStrokeColorRGB(0,0,0)
    pdf.rect(363, height - 210, width=190, height=130, fill=True)
    pdf.setStrokeAlpha(1)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setFillAlpha(1)  # 투명도를 다시 기본값으로 설정
    user_loc_img = geo_visualization.get_user_loc_map(target_area)
    user_loc_img_data = ImageReader(user_loc_img)
    pdf.drawImage(user_loc_img_data, 360, height - 207, width=190, height=130)


    ## 설명
    reg_img=lin_reg.get_reg_img(target_area)
    pdf.setFillColorRGB(0.23529411764705882,0.23529411764705882,0.23529411764705882)
    pdf.setFont("한컴고딕보통", 9)
    introduce = (" 본 분석 과제는 랜덤 포레스트(Random Forest) 기법을 사용하여 당년 특정 행정동 점포당 예상 매출액을 예측함. 랜덤포레스트기법은 앙상블 학습 기법 중 하나로, 다수의 의사 결정 트리(Decision Trees)를 사용하여 예측 성능을 향상시키는 방법임. 그리고 사용자가 선정한 행정동의 과거 연도별 매출액 데이터 분포를 통해 당해 예상 매출액을 회귀예측하여, 랜덤포레스트 기법을 통해 생성한 예측값과 비교 실시. 서비스의 신뢰성을 확보하기 위해, 비교 결과 두 예측값의 차이가 크지 않은 경우에만 대하여 예상 매출액 추정 서비스를 제공함.")
    introduce=textwrap.wrap(introduce, width=50)
    text_object = pdf.beginText(200, height - 230)  # 텍스트가 출력될 좌표를 조정
    text_object.textLines(introduce)
    pdf.drawText(text_object)
    # 이미지 배치
    pdf.drawImage(reg_img, 30, height - 340, width=160, height=120)


    # 사용되는 피처
    schema=r"C:\Users\Playdata\Desktop\our_pos_data\캡처\스키마.png"

    pdf.setFont("맑은고딕", 9)
    use_feature= (" 예측 변수의 선정은 결과 변수에 유의미한 영향을 줄 것으로 예상되는 행정동별 지하철/버스 승하차인원, 생활인구, 인당 연간 총 소득 금액 및 지출 총금액, 점포수를 선정. 유사한 주제의 선행 연구를 고려하여, 연구 지역의 금융기관의 수가 패스트푸드 매출액에 유의미한 영향을 미친다는 결론 반영, 금융기관 소계 예측 변수 포함.")
    introduce=textwrap.wrap(use_feature, width=70)
    text_object = pdf.beginText(30, height - 360)  # 텍스트가 출력될 좌표를 조정
    text_object.textLines(introduce)
    pdf.drawText(text_object)
    # 이미지 배치
    pdf.drawImage(schema, 50, height - 460, width=500, height=70)



    # 유동인구, 교통량 등에 대한 시각화 및 데이터
    pdf.setFont("맑은고딕", 9)
    data= (" 결과 변수인 당년 예상 매출액과 가장 높은 피어슨-상관 계수를 갖는 예측 변수인 교통량, 생활인구를 시각화한 HeatMap은 다음과 같음.")
    introduce=textwrap.wrap(data, width=70)
    text_object = pdf.beginText(30, height - 480)  # 텍스트가 출력될 좌표를 조정
    text_object.textLines(introduce)
    pdf.drawText(text_object)
    # 이미지 그림자 배치
    pdf.setFillColorRGB(0,0,0)  # RGB 색상 지정
    pdf.setFillAlpha(0.05)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setStrokeAlpha(0.05)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setStrokeColorRGB(0,0,0)
    pdf.rect(53, height - 653, 225, 150, fill=True)
    pdf.rect(323, height - 653, 225, 150, fill=True)
    pdf.setStrokeAlpha(1)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setFillAlpha(1)  # 투명도를 다시 기본값으로 설정
    # 이미지 배치
    ingu_img = geo_visualization.get_map('총생활인구수', target_area)
    ingu_img_data = ImageReader(ingu_img)
    pdf.drawImage(ingu_img_data, 50, height - 650, width=225, height=150)
    traffic_img = geo_visualization.get_map('교통량이용수', target_area)
    traffic_img_data = ImageReader(traffic_img)
    pdf.drawImage(traffic_img_data, 320, height - 650, width=225, height=150)


    # 유동인구, 교통량 등에 대한 시각화 및 데이터
    pdf.setFillColorRGB(0.09019607843137255, 0.1450980392156863, 0.42745098039215684)
    pdf.setFont("맑은고딕", 9)
    err_exp= (" 오차의 분포는 positive-skewed분포이며, 이를 로그변환을 통해 정규분포에 가깝게 나타낸 모습은 좌측 사진과 같음. 오차 분포의 95% 신뢰구간을 구하기 위해 정규성을 검증한 후, 이를 다시 역변환하여 각 구간의 끝값을 도출함. 정규성 검증 결과, 정성적 검증 수단인 QQ Plot은 우측 사진과 같으며, 정량적 검증 수단인 Shapiro-wilk 검정 결과는 다음과 같음.")
    err_exp=textwrap.wrap(err_exp, width=35)
    text_object = pdf.beginText(300, height - 672)  # 텍스트가 출력될 좌표를 조정
    text_object.textLines(err_exp)
    pdf.drawText(text_object)
    # 이미지 그림자 배치
    pdf.setFillColorRGB(0,0,0)  # RGB 색상 지정
    pdf.setFillAlpha(0.05)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setStrokeAlpha(0.05)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setStrokeColorRGB(0,0,0)
    pdf.rect(32, height - 772, 125, 105, fill=True)
    pdf.rect(167, height - 772, 125, 105, fill=True)
    pdf.setStrokeAlpha(1)  # 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
    pdf.setFillAlpha(1)  # 투명도를 다시 기본값으로 설정
    #히스토그램,그래프,샤피로검정결과 저장
    err_hist_path, qq_path, shapiro_test=err_vis.get_err_vis()
    # 이미지 배치
    pdf.drawImage(err_hist_path, 30, height - 770, width=125, height=105)
    # 이미지 배치
    pdf.drawImage(qq_path, 165, height - 770, width=125, height=105)
    #샤피로 테스트 출력
    pdf.setFont("한컴고딕굵게", 9)
    statistic_str = "Statistic : " + str(shapiro_test[0])
    p_value_str = "p-value : " + str(shapiro_test[1])
    text_stat1 = pdf.beginText(300, height - 750)
    text_stat1.textLine(statistic_str)
    pdf.drawText(text_stat1)
    text_stat2 = pdf.beginText(300, height - 760)
    text_stat2.textLine(p_value_str)
    pdf.drawText(text_stat2)

    print("pdf 생성 완료")

    pdf.save()