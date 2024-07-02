<template>
  <div class="modal-overlay" v-if="isOpen">
    <div class="modal-content">
      <div class="modal-bar">
        <span>{{ title }}</span>
        <button @click="$emit('close')">X</button>
      </div>
      <div class="modal-body">
        <slot></slot> <!-- Placeholder for other modal content -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" @click="$emit('confirm')">확인</button>
        <button type="button" class="btn btn-secondary" @click="$emit('close')">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    required: true
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
  width: 600px; /* Increased width */
  max-height: 90vh; /* Ensure it fits within the viewport */
  overflow-y: auto; /* Enable scrolling if content overflows */
  position: relative;
}

.modal-bar {
  background-color: #28282B;
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

.modal-footer {
  display: flex;
  justify-content: center;
  gap: 10px;
  padding: 10px;
  border-top: 1px solid #dee2e6;
}


</style>
