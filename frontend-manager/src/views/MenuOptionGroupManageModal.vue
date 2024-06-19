<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <header class="modal-header">
        <slot name="header">Default Header</slot>
      </header>
      <section class="modal-body">
        <slot name="body">Default Body</slot>
      </section>
      <footer class="modal-footer">
        <slot name="footer">
          <MaterialButton class="btn btn-secondary" @click="closeModal">Close</MaterialButton>
        </slot>
      </footer>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['update:modelValue']);

const closeModal = () => {
  emit('update:modelValue', false);
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 500px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-header,
.modal-footer {
  padding-bottom: 10px;
}

.modal-body {
  padding: 10px 0;
}
</style>
