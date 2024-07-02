<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal-content">
      <div class="modal-bar">
        <div class="d-flex justify-content-between align-items-center">
          <h5 class="modal-title">{{ title }}</h5>
        </div>
      </div>
      <div class="modal-body">
        <slot></slot>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" @click="confirmAction">Confirm</button>
        <button type="button" class="btn btn-secondary" @click="closeModal">Cancel</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isOpen: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      required: true
    }
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    confirmAction() {
      this.$emit('confirm');
    }
  }
};
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
}

.modal-footer {
  display: flex;
  justify-content: center;
  gap: 10px;
  padding: 10px;
  border-top: 1px solid #dee2e6;
}

.POS-name {
  font-size: 1.25rem; /* Increased font size */
  color: aliceblue;
}

.modal-title {
  font-size: 1.25rem;
  color: #dee2e6;
}
</style>
