<template>
  <div class="modal-overlay" v-if="isOpen">
    <div class="modal-content">
      <div class="modal-bar">
        <span>{{ title }}</span>
      </div>
      <div class="modal-body">
        <div v-if="validationEnabled" class="form-group">
          <label for="category-name">추가할 카테고리 이름</label>
          <input 
            id="category-name" 
            type="text" 
            class="form-control" 
            v-model="categoryName" 
            @input="validateInput" 
            placeholder="카테고리 이름을 입력하세요">
          <span v-if="errorMessage" class="error-message">{{ errorMessage }}</span>
        </div>
        <slot></slot> <!-- Placeholder for other modal content -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" @click="confirmAction">확인</button>
        <button type="button" class="btn btn-secondary" @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  validationEnabled: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['close', 'confirm']);

const categoryName = ref('');
const errorMessage = ref('');

const closeModal = () => {
  emit('close');
};

const confirmAction = () => {
  if (props.validationEnabled && errorMessage.value) {
    return;
  }
  emit('confirm', props.validationEnabled ? categoryName.value : null);
};

const validateInput = () => {
  const regex = /^[a-zA-Z]*$/;
  if (!regex.test(categoryName.value)) {
    errorMessage.value = '영어만 입력 가능합니다';
  } else {
    errorMessage.value = '';
  }
};

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    categoryName.value = '';
    errorMessage.value = '';
  }
});
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 0;
  border-radius: 5px;
  width: 400px;
  position: relative;
  overflow: hidden;
}

.modal-bar {
  background-color: #3f51b5;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
}

.modal-body {
  padding: 20px;
  font-weight: bold;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.form-group {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.form-control {
  margin-bottom: 10px;
  height: 40px;
  width: 100%;
  border: 1px solid #ced4da;
  border-radius: 4px;
}

.error-message {
  color: red;
  font-size: 0.875em;
}

.modal-footer {
  display: flex;
  justify-content: center;
  gap: 10px;
  padding: 10px;
  border-top: 1px solid #dee2e6;
}

.modal-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-bottom: 1px solid #dee2e6;
  padding-bottom: 10px;
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: white;
}

.modal-title {
  font-size: 1.25rem;
  color: #dee2e6;
}
</style>
